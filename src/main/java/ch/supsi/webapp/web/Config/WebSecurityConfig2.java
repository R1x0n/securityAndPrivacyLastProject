package ch.supsi.webapp.web.Config;
import ch.supsi.webapp.web.Utilities.JwtAuthenticationEntryPoint;
import ch.supsi.webapp.web.Utilities.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        http.antMatcher("/api/**")
                .authorizeRequests().anyRequest().hasRole("MANAGER")
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider baseAuthProvider2() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }
   // @Bean
   // public DaoAuthenticationProvider jwtAuthProvider2() {
   //     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
   //     authProvider.setUserDetailsService(jwtUserDetailsService);
   //     authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
   //     return authProvider;
   // }

    @Bean
    public AuthenticationManager authManager2(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(baseAuthProvider2())
                .build();
    }

}

