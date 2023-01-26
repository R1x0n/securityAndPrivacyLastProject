package ch.supsi.webapp.web.Config;
import ch.supsi.webapp.web.Utilities.JwtAuthenticationEntryPoint;
import ch.supsi.webapp.web.Utilities.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(2)
@EnableWebSecurity
public class WebSecurityConfig2 {



    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Qualifier("jwtUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
       http.authorizeRequests()
               .antMatchers("/api/login").permitAll()
                .antMatchers("/api/costumers/all").hasRole("NORMAL")
                .antMatchers("/api/**").authenticated();
        http.csrf().disable();

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider baseAuthProvider2() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager2(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(baseAuthProvider2())
                .build();
    }

}

