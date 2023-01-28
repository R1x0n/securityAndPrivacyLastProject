package ch.supsi.webapp.web.Utilities;

import ch.supsi.webapp.web.Modules.Employee;
import ch.supsi.webapp.web.Services.ServiceUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    static ServiceUser serviceUser = null;

    public UserConverter(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    public static Employee getUser(Object principal) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User user = (User) principal;
            return serviceUser.findUserByEmail(user.getUsername());
        }
        Employee user = new Employee();
        user.setUsername("Anonymous");
        return user;
    }
}
