package ch.supsi.webapp.web.Controllers;

import ch.supsi.webapp.web.Config.JwtUserDetailsService;
import ch.supsi.webapp.web.Interfaces.ICustomer;
import ch.supsi.webapp.web.Modules.Employee;
import ch.supsi.webapp.web.Repositories.CostumerRepository;
import ch.supsi.webapp.web.Repositories.LogsRepository;
import ch.supsi.webapp.web.Services.ServiceUser;
import ch.supsi.webapp.web.Utilities.JwtRequestModel;
import ch.supsi.webapp.web.Utilities.JwtResponseModel;
import ch.supsi.webapp.web.Utilities.UtilLogger;
import ch.supsi.webapp.web.Utilities.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@Controller
public class TemplateController {

    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private LogsRepository logsRepository;

    @PostMapping("/api/login")
    public ResponseEntity createToken(@RequestBody JwtRequestModel
                                              request) throws Exception {
        request.setUsername(java.net.URLDecoder.decode(request.getUsername(), StandardCharsets.UTF_8));
        request.setPassword(java.net.URLDecoder.decode(request.getPassword(), StandardCharsets.UTF_8));
        try {
            authenticationManager.authenticate(
                    new
                            UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            );
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);
        return ResponseEntity.ok(new JwtResponseModel(jwtToken));
    }


    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private ServiceUser serviceUser;
    @Autowired
    private CostumerRepository costumerRepository;

    private Employee getUser(Object principal) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User user = (User) principal;
            return serviceUser.findUserByEmail(user.getUsername());
        }
        Employee user = new Employee();
        user.setUsername("Anonymous");
        return user;
    }

    @Controller
    public class MyErrorController implements ErrorController {

        @RequestMapping("/error")
        public String handleError(Model model, HttpServletRequest request) {
            model.addAttribute("error", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
            return "error";
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homeRedirect(Model model) {
        return "redirect:/app";
    }

    @RequestMapping(value = "/app/allCustomers", method = RequestMethod.GET)
    public String home(Model model) {
        List<ICustomer> customers = costumerRepository.getCustomersInterface();
        model.addAttribute("customers", costumerRepository.getCustomersInterface());
        model.addAttribute("user", getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        return "allCustomers";
    }

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public String personalCustomers(Model model) {
        List<ICustomer> customers = costumerRepository.getPersonalCustomersInterface(getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmployeeId());
        model.addAttribute("customers", customers);
        model.addAttribute("user", getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        return "index";
    }

    @RequestMapping(value = "/app/login", method = RequestMethod.GET)
    public String login(Model model) throws SQLException, IOException {
        UtilLogger logger = new UtilLogger(logsRepository);
        logger.info("Login page",getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmployeeId());
        return "login";
    }

    @RequestMapping(value = "/ticket/new", method = RequestMethod.GET)
    public String newTicket(Model model) {
        return "createTicketForm";
    }

    @RequestMapping(value = "/app/changePassword", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("user", getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        return "changePassword";
    }

    @RequestMapping(value = "/app/changePassword", method = RequestMethod.POST)
    public String registerPost(@RequestParam("current") String current, @RequestParam("new") String newPass, @RequestParam("confirm") String confirm, Model model) throws SQLException, IOException {
        Employee user = getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String password = user.getPassword();
        if (current.equals(newPass)) {
            model.addAttribute("error", "The new password must be different from the current one");
            model.addAttribute("user", getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
            return "changePassword";
        }
        if (validatePassword(newPass)) {
            if (newPass.equals(confirm)) {
                if (encoder.matches(current, password)) {
                    user.setPassword(encoder.encode(newPass));
                    serviceUser.save(user);
                    //Log4j.getInstance().getLogger().info("User " + user.getUsername() + " changed password");
                    return "redirect:/app/profile";
                } else {
                    model.addAttribute("error", "Current password is not correct");
                    model.addAttribute("user", getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
                    return "changePassword";
                }
            } else {
                model.addAttribute("error", "New password and confirm password are not the same");
                model.addAttribute("user", getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
                return "changePassword";
            }
        } else {
            model.addAttribute("error", "Password must be at least 8 characters long and contain at least one digit, one uppercase letter and one lowercase letter");
            model.addAttribute("user", getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
            return "changePassword";
        }
    }

    //Method to validate the password complexity
    private boolean validatePassword(String password) {
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasNumber = password.matches(".*\\d.*");
        return hasUppercase && hasLowercase && hasNumber && password.length() >= 8;
    }


    @RequestMapping(value = "/app/profile", method = RequestMethod.GET)
    public String profile(Model model) {
        model.addAttribute("user", getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        return "profile";
    }


}
