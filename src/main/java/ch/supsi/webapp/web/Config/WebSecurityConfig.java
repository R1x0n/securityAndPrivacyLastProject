package ch.supsi.webapp.web.Config;

import ch.supsi.webapp.web.Handler.CustomLoginFailureHandler;
import ch.supsi.webapp.web.Handler.CustomLoginSuccessHandler;
import ch.supsi.webapp.web.Handler.CustomLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@Order(1)
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private CustomLoginFailureHandler loginFailureHandler;
    @Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;
    @Autowired
    private CustomLogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/app/login").permitAll()
                .antMatchers("/app/allCustomers").hasRole("MANAGER")
                .antMatchers("/app/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/app/login")
                .failureUrl("/app/login?error")
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
                .and()
                .logout()
                .logoutSuccessHandler(logoutHandler)
                .logoutUrl("/app/logout")
                .logoutSuccessUrl("/app");
        http.csrf().disable();

        return http.build();
    }

    public DaoAuthenticationProvider baseAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    @Primary
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(baseAuthProvider())
                .build();
    }

}

