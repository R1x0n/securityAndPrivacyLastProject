package ch.supsi.webapp.web.Controllers;

import ch.supsi.webapp.web.Modules.Employee;
import ch.supsi.webapp.web.Services.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ServiceUser serviceUser;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<Employee> get() {
        return (List<Employee>) serviceUser.findAll();
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Employee> post(@RequestBody Employee user) {
        serviceUser.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public Optional<Employee> getTicket(@PathVariable int id) {
        Optional<Employee> user = serviceUser.findById(id);
        if (user.isPresent()) {
            return user;
        } else {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
    public Optional<Employee> updateTicket(@PathVariable int id, @RequestBody Employee user) {
        Optional<Employee> res = Optional.ofNullable(serviceUser.save(user));
        if (res.isPresent()) {
            return res;
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