package ch.supsi.webapp.web.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/app/**")
                .authorizeRequests()
                .mvcMatchers("/app/allCustomers").hasRole("MANAGER")
                .mvcMatchers("/app/login").permitAll()
                .mvcMatchers("/app/login2").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/app/login")
                .failureUrl("/app/login?error")
                .and()
                .logout()
                .logoutUrl("/app/logout")
                .logoutSuccessUrl("/app");
        http.csrf().disable();

        return http.build();
    }

    @Bean
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

