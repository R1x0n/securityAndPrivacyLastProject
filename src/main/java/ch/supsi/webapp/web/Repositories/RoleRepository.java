package ch.supsi.webapp.web.Repositories;

import ch.supsi.webapp.web.Modules.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}