package org.iqmanager.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/** Модель данных для входа пользователя */

@Entity
@Table(name = "user_login_data")
@Getter
@Setter
@NoArgsConstructor
public class UserLoginData implements UserDetails, Serializable, GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /** Логин */
    @Column(name = "phone")
    private String username;

    /** Пароль */
    @Column(name = "password")
    private String password;

    /** Данные пользователя */
    @OneToOne(mappedBy = "userLoginData", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private UserData userData;

    public UserLoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
        userData.setUserLoginData(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**Сервис для пользователей, так что роль может быть только user*/
    @Override
    public String getAuthority() {
        return "USER";
    }

    @Override
    public String toString() {
        return "UserLoginData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoginData that = (UserLoginData) o;
        return id == that.id && Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }
}
