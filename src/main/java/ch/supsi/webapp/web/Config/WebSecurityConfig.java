package ch.supsi.webapp.web.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/login2").permitAll()
                .mvcMatchers("/ticket/new").authenticated()
                .mvcMatchers("/ticket/*/edit").authenticated()
                .mvcMatchers("/ticket/*/delete").hasRole("ADMIN")
                .mvcMatchers("/ticket/**").permitAll()
                .mvcMatchers("/css/**").permitAll()
                .mvcMatchers("/img/**").permitAll()
                .mvcMatchers("/js/**").permitAll()
                .mvcMatchers("/lib/**").permitAll()
                .mvcMatchers("/webjars/**").permitAll()
                .mvcMatchers("/fonts/**").permitAll()
                .mvcMatchers("/login", "/register").permitAll()
                .mvcMatchers("/tickets/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
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
    @Bean
    public DaoAuthenticationProvider jwtAuthProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jwtUserDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(baseAuthProvider())
                .authenticationProvider(jwtAuthProvider())
                .build();
    }

}

