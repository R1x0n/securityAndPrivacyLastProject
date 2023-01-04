package ch.supsi.webapp.web.Modules;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @Column(name = "employeeId")
    private int employeeId;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "Email")
    private String email;

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Title")
    private String title;

    @Column(name = "ReportsTo")
    private Integer reportsTo;

    @Column(name = "BirthDate")
    private Date birthDate;

    @Column(name = "HireDate")
    private Date hireDate;

    @Column(name = "Address")
    private String address;

    @Column(name = "City")
    private String city;

    @Column(name = "State")
    private String state;

    @Column(name = "Country")
    private String country;

    @Column(name = "PostalCode")
    private String postalCode;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Fax")
    private String fax;

    @ManyToOne
    Role role;

    @Override
    public String toString() {
        return username;
    }

    public String getName() {
        return firstName;
    }
}