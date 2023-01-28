package ch.supsi.webapp.web.Controllers;

import ch.supsi.webapp.web.Interfaces.ICustomer;
import ch.supsi.webapp.web.Modules.Employee;
import ch.supsi.webapp.web.Repositories.CostumerRepository;
import ch.supsi.webapp.web.Services.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}