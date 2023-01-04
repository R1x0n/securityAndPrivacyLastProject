package ch.supsi.webapp.web.Controllers;

import ch.supsi.webapp.web.Config.JwtUserDetailsService;
import ch.supsi.webapp.web.Modules.Employee;
import ch.supsi.webapp.web.Repositories.RoleRepository;
import ch.supsi.webapp.web.Services.ServiceUser;
import ch.supsi.webapp.web.Utilities.JwtRequestModel;
import ch.supsi.webapp.web.Utilities.JwtResponseModel;
import ch.supsi.webapp.web.Utilities.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.nio.charset.StandardCharsets;

@Controller
public class TemplateController {

    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/login2")
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


    private PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private ServiceUser serviceUser;
    @Autowired
    private RoleRepository roleRepository;

    private Employee getUser(Object principal) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User user = (User) principal;
            return serviceUser.findUserByEmail(user.getUsername());
        }
        Employee user = new Employee();
        user.setUsername("Anonymous");
        return user;
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("user", getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "/ticket/new", method = RequestMethod.GET)
    public String newTicket(Model model) {
        return "createTicketForm";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(Employee user, @RequestParam("password2") String password2) {
        if (serviceUser.findUserByUsername(user.getUsername()) == null && user.getPassword().equals(password2)) {
            user.setRole(roleRepository.findById(2).get());
            user.setPassword(encoder.encode(user.getPassword()));
            serviceUser.save(user);
            return "redirect:/login";
        }
        return "register";
    }


}
