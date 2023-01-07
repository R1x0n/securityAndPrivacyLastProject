package ch.supsi.webapp.web.Config;

import ch.supsi.webapp.web.Utilities.JwtAuthenticationEntryPoint;
import ch.supsi.webapp.web.Utilities.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//@Order(2)
//@EnableWebSecurity
//public class WebSecurityConfigJwt extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private JwtAuthenticationEntryPoint authenticationEntryPoint;
//    @Qualifier("jwtUserDetailsService")
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Autowired
//    private JwtFilter filter;
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws
//            Exception {
//        return super.authenticationManagerBean();
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeRequests().antMatchers("/login2","/login").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//    }
//}