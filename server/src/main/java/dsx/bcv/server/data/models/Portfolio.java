package dsx.bcv.server.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "portfolios")
@Data
@NoArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Имя портфеля
     */
    @Column(nullable = false)
    private String name;

    /**
     * Список сделок в портфеле
     */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Trade> trades = new HashSet<>();

    /**
     * Список транзакций в портфеле
     */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public Portfolio(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
