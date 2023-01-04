package ch.supsi.webapp.web.Config;


import ch.supsi.webapp.web.Modules.Employee;
import ch.supsi.webapp.web.Services.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private ServiceUser ticketService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee user = ticketService.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<GrantedAuthority> auth = AuthorityUtils.createAuthorityList(user.getRole().getName());
        return new org.springframework.security.core.userdetails.User(email, user.getPassword(), true, true, true, true, auth);
    }

}