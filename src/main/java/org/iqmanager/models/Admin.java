package org.iqmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/** Модель администратора */
@Entity
@Table(name = "admin_data")
@Getter
@Setter
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Имя */
    @Column(name = "name")
    private String name;

    /** Телефон */
    @Column(name = "last_name")
    private String phone;


    /** Роль */
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "admin_x_role",
            joinColumns = @JoinColumn(name = "id_admin"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    @JsonIgnore
    private List<AdminRole> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Admin admin = (Admin) o;
        if (id != admin.id) return false;
        if (!Objects.equals(name, admin.name)) return false;
        return Objects.equals(phone, admin.phone);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }
}
