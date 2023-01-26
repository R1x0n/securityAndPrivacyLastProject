package ch.supsi.webapp.web.Modules;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Getter
@Setter
@Entity
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "employeeId", nullable = false)
    private int employeeId;

    @Column(name = "level", nullable = false)
    private String level;

    @Column(name = "message", nullable = false)
    private String message;

}
