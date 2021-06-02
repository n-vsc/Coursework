package dsx.bcv.marketdata_provider.services.quote_providers.dsx.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph.DsxCurrencyVertex;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"baseCurrency", "quotedCurrency"})
public class DsxInstrument {

    private String currencyPair;
    private DsxCurrencyVertex quotedCurrency;
    private DsxCurrencyVertex baseCurrency;

    @JsonCreator
    public DsxInstrument(
            @JsonProperty("id") String currencyPair,
            @JsonProperty("baseCurrency") String baseCurrency,
            @JsonProperty("quoteCurrency") String quotedCurrency
    ) {
        this.currencyPair = currencyPair;
        this.baseCurrency = new DsxCurrencyVertex(baseCurrency.toLowerCase());
        this.quotedCurrency = new DsxCurrencyVertex(quotedCurrency.toLowerCase());
    }
}
