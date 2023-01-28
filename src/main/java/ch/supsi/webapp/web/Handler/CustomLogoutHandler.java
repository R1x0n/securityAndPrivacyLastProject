package ch.supsi.webapp.web.Handler;

import ch.supsi.webapp.web.Services.ServiceUser;
import ch.supsi.webapp.web.Utilities.UtilLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private ServiceUser customerService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        User userDetails = (User) authentication.getPrincipal();
        String username = userDetails.getUsername();

        UtilLogger.info("Logout success " + username);
        super.onLogoutSuccess(request, response, authentication);
    }
}