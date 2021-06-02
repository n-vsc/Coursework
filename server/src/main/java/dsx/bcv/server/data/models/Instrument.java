package dsx.bcv.server.data.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "instruments")
@Table(uniqueConstraints = {
        //@UniqueConstraint(columnNames = {"base_currency", "quoted_currency"})
})
@Data
@NoArgsConstructor
public class Instrument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Обмениваемый актив
     */
    @ManyToOne
    private Currency baseCurrency;

    /**
     * Запрашиваемый актив
     */
    @ManyToOne
    private Currency quotedCurrency;

    public Instrument(Currency baseCurrency, Currency quotedCurrency) {
        this.baseCurrency = baseCurrency;
        this.quotedCurrency = quotedCurrency;
    }

    public Instrument(String instrument) {

        var baseCurrency = new Currency(instrument.substring(0, 3));
        var quotedCurrency = new Currency(instrument.substring(3, 6));

        this.baseCurrency = baseCurrency;
        this.quotedCurrency = quotedCurrency;
    }

    @Override
    public String toString() {
        return baseCurrency + "-" + quotedCurrency;
    }
}
