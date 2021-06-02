package dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph;

import dsx.bcv.marketdata_provider.services.quote_providers.dsx.models.DsxInstrument;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DsxInstrumentEdge {

    private final DsxCurrencyVertex baseCurrency;
    private final DsxCurrencyVertex quotedCurrency;
    private final boolean reversed;

    public DsxInstrumentEdge(DsxInstrument dsxInstrument) {
        baseCurrency = dsxInstrument.getBaseCurrency();
        quotedCurrency = dsxInstrument.getQuotedCurrency();
        reversed = false;
    }

    @Override
    public String toString() {
        return baseCurrency.toString() + quotedCurrency.toString();
    }

    public DsxInstrumentEdge reverse() {
        return new DsxInstrumentEdge(
                baseCurrency,
                quotedCurrency,
                !reversed
        );
    }
}
