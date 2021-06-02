package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.data.models.Ticker;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.models.DsxTicker;
import org.springframework.core.convert.converter.Converter;

public class DsxTickerToTickerConverter implements Converter<DsxTicker, Ticker> {
    @Override
    public Ticker convert(DsxTicker dsxTicker) {
        return new Ticker(
                new Asset("unknown"),
                dsxTicker.getExchangeRate(),
                0
        );
    }
}
