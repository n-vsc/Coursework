package dsx.bcv.marketdata_provider.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "assets")
@Data
@NoArgsConstructor
public class Asset {

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
    @Column(unique = true)
    private String name;

    public Asset(String code) {
        this(code, "unknown");
    }

    public Asset(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return code;
    }
}
