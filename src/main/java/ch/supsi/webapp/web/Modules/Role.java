package ch.supsi.webapp.web.Modules;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    @Override
    public String toString() {
        return removePrefix(name);
    }

    private static String removePrefix(String s) {
        if (s != null && s.startsWith("ROLE_")) {
            return s.substring("ROLE_".length());
        }
        return s;
    }
}