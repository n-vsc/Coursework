package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Ticker;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models.AlphaVantageTicker;
import org.springframework.core.convert.converter.Converter;

public class AlphaVantageTickerToTickerConverter implements Converter<AlphaVantageTicker, Ticker> {
    @Override
    public Ticker convert(AlphaVantageTicker source) {
        return new Ticker(
                new AlphaVantageAssetToAssetConverter().convert(source.getBaseAsset()),
                source.getExchangeRate(),
                0
        );
    }
}
