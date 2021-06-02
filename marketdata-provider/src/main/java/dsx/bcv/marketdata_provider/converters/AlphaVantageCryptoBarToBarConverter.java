package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Bar;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models.AlphaVantageCryptoBar;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.ZoneOffset;

public class AlphaVantageCryptoBarToBarConverter
        implements Converter<AlphaVantageCryptoBar, Bar> {
    @Override
    public Bar convert(AlphaVantageCryptoBar source) {
        return new Bar(
                new AlphaVantageAssetToAssetConverter().convert(source.getAsset()),
                source.getExchangeRate(),
                source.getDate().toEpochSecond(LocalTime.ofSecondOfDay(0), ZoneOffset.UTC)
        );
    }
}
