package ch.supsi.webapp.web.Services;

import ch.supsi.webapp.web.Modules.Role;
import ch.supsi.webapp.web.Modules.Employee;
import ch.supsi.webapp.web.Repositories.RoleRepository;
import ch.supsi.webapp.web.Repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceUser {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    public ServiceUser( UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Iterable<Employee> findAll() {
        return userRepository.findAll();
    }

    public Employee save(Employee user) {
        return userRepository.save(user);
    }

    public Optional<Employee> findById(long id) {
        return userRepository.findById(String.valueOf(id));
    }

    public Optional<Employee> deleteById(int id) {
        userRepository.deleteById(String.valueOf((long) id));
        return Optional.empty();
    }

    public List<Employee> getUsers() {
        return userRepository.findAll();
    }

    public Employee findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public Employee findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void postUser(Employee user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(roleRepository.findById(2).get());
        userRepository.save(user);
    }
}