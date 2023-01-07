package ch.supsi.webapp.web.Repositories;

import ch.supsi.webapp.web.Interfaces.ICustomer;
import ch.supsi.webapp.web.Modules.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Employee,Integer> {
    @Query(value="SELECT c.firstName AS CustomerFirstName, c.lastName AS CustomerLastName, c.email AS CustomerEmail, c.phone AS CustomerPhone FROM Customer AS c", nativeQuery = true)
    List<ICustomer> getCustomersInterface();


    @Query(value="SELECT c.firstName AS CustomerFirstName, c.lastName AS CustomerLastName, c.email AS CustomerEmail, c.phone AS CustomerPhone FROM Customer AS c where c.SupportRepId = ?1", nativeQuery = true)
    List<ICustomer> getPersonalCustomersInterface(Integer SupportRepId);
}
