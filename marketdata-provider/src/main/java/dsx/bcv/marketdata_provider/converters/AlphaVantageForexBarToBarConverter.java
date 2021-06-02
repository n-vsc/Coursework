package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Bar;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models.AlphaVantageForexBar;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.ZoneOffset;

public class AlphaVantageForexBarToBarConverter
        implements Converter<AlphaVantageForexBar, Bar> {
    @Override
    public Bar convert(AlphaVantageForexBar source) {
        return new Bar(
                new AlphaVantageAssetToAssetConverter().convert(source.getAsset()),
                source.getExchangeRate(),
                source.getDate().toEpochSecond(LocalTime.ofSecondOfDay(0), ZoneOffset.UTC)
        );
    }
}
