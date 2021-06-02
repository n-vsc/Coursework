package dsx.bcv.marketdata_provider.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "bars")
@Data
@NoArgsConstructor
public class Bar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Базовый актив в инструменте, обмениваемый всегда равен usd
     */
    @ManyToOne
    private Asset baseAsset;

    /**
     * Курс актива по отношению к доллару на момет времени в timestamp
     */
    @Column(precision = 20, scale = 10)
    private BigDecimal exchangeRate;

    /**
     * Дата, на которую актуален курс в exchangeRate
     */
    private long timestamp;

    public Bar(Asset baseAsset, BigDecimal exchangeRate, long timestamp) {
        this.baseAsset = baseAsset;
        this.exchangeRate = exchangeRate;
        this.timestamp = timestamp;
    }
}
