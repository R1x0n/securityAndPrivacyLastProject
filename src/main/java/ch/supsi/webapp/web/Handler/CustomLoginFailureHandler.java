package ch.supsi.webapp.web.Handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.supsi.webapp.web.Modules.Employee;
import ch.supsi.webapp.web.Services.ServiceUser;
import ch.supsi.webapp.web.Utilities.UtilLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private ServiceUser userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        UtilLogger.warning("Login failure for user " + username);
        super.setDefaultFailureUrl("/app/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
