package dsx.bcv.server.views;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import dsx.bcv.server.exceptions.NotFoundException;
import lombok.Data;

@Data
public class InstrumentVO {

    private CurrencyVO baseCurrency;
    private CurrencyVO quotedCurrency;

    public InstrumentVO(CurrencyVO baseCurrency, CurrencyVO quotedCurrency) {
        this.baseCurrency = baseCurrency;
        this.quotedCurrency = quotedCurrency;
    }

    public InstrumentVO(String instrument) {
        var currencyPair = Lists.newArrayList(Splitter.on("-").split(instrument));
        if (currencyPair.size() != 2) {
            throw new NotFoundException("Invalid instrument");
        }
        var baseCurrency = new CurrencyVO(currencyPair.get(0));
        var quotedCurrency = new CurrencyVO(currencyPair.get(1));

        this.baseCurrency = baseCurrency;
        this.quotedCurrency = quotedCurrency;
    }

    @Override
    public String toString() {
        return baseCurrency + "-" + quotedCurrency;
    }
}
