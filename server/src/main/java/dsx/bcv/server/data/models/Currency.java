package dsx.bcv.server.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "currencies")
@Data
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Тикер
     */
    @Column(unique = true)
    private String code;

    /**
     * Полное имя
     */
    //@Column(unique = true)
    private String name;

    public Currency(String code) {
        this(code, "unknown");
    }

    public Currency(String code, String name) {
        this.code = code.toLowerCase();
        this.name = name;
    }

    @Override
    public String toString() {
        return code;
    }
}
