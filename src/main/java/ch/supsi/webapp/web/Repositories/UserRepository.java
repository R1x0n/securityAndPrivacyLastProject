package ch.supsi.webapp.web.Repositories;

import ch.supsi.webapp.web.Modules.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

@Qualifier("repository")
public interface UserRepository extends JpaRepository<Employee, String> {
    Employee findUserByUsername(String username);
    Employee findUserByEmail(String email);
}