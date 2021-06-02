package dsx.bcv.marketdata_provider.services.quote_providers.dsx;

import dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph.DsxCurrencyVertex;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class DsxSupportedCurrencies {

    @Getter
    private final Set<DsxCurrencyVertex> currencies;

    private DsxSupportedInstruments dsxSupportedInstruments;

    public DsxSupportedCurrencies(DsxSupportedInstruments dsxSupportedInstruments) {
        this.dsxSupportedInstruments = dsxSupportedInstruments;
        currencies = initSupportedCurrencies();
        log.info("Dsx supported currencies: {}", currencies);
    }

    private Set<DsxCurrencyVertex> initSupportedCurrencies() {

        var supportedInstruments = dsxSupportedInstruments.getInstruments();

        var currencies = new HashSet<DsxCurrencyVertex>();
        supportedInstruments.forEach(dsxInstrument -> currencies.add(dsxInstrument.getBaseCurrency()));
        supportedInstruments.forEach(dsxInstrument -> currencies.add(dsxInstrument.getQuotedCurrency()));

        return currencies;
    }
}
