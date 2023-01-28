package ch.supsi.webapp.web.Controllers;

import ch.supsi.webapp.web.Interfaces.ICustomer;
import ch.supsi.webapp.web.Modules.Employee;
import ch.supsi.webapp.web.Repositories.CostumerRepository;
import ch.supsi.webapp.web.Services.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
public class HttpUserController {
    @Autowired
    private CostumerRepository costumerRepository;
    @Autowired
    private ServiceUser serviceUser;

    private Employee getUser(Object principal) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User user = (User) principal;
            return serviceUser.findUserByEmail(user.getUsername());
        }
        Employee user = new Employee();
        user.setUsername("Anonymous");
        return user;
    }
    @RequestMapping(value = "/api/costumers/all", method = RequestMethod.GET)
    public List<ICustomer> getAllCostumers() {
        return costumerRepository.getCustomersInterface();
    }

    @RequestMapping(value = "/api/costumers/", method = RequestMethod.GET)
    public List<ICustomer> getCostumers() {
        return costumerRepository.getPersonalCustomersInterface(getUser(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmployeeId());
    }

    @RequestMapping(value = "/api/costumers", method = RequestMethod.POST)
    public ResponseEntity<Employee> post(@RequestBody Employee user) {
        serviceUser.save((Employee) costumerRepository.getCustomersInterface());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public Optional<Employee> getTicket(@PathVariable int id) {
        Optional<Employee> user = serviceUser.findById(id);
        if (user.isPresent()) {
            return serviceUser.findById(id);
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public Optional<Employee> updateTicket(@PathVariable int id, @RequestBody Employee user) {
        if (Optional.ofNullable(serviceUser.save(user)).isPresent()) {
            return Optional.ofNullable(serviceUser.save(user));
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Map<String, String>> deleteTicket(@PathVariable int id) {
        if (serviceUser.findById(id).isPresent()) {
            serviceUser.deleteById(id);
            HashMap<String, String> res = new HashMap<>();
            res.put("status", "success");
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(NO_CONTENT, "Unable to find resource");
        }
    }
}