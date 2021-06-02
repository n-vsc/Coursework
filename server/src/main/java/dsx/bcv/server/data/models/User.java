package dsx.bcv.server.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Уникальное имя пользователя
     */
    @Column(unique = true)
    private String username;

    /**
     * Зашифрованный пароль пользователя
     */
    private String password;

    /**
     * Email пользователя
     */
    private String email;

    /**
     * Список портфелей пользователя
     */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Portfolio> portfolios;

    /**
     * Список ролей пользователя
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
