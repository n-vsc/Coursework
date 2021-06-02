package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.data.models.Bar;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.models.DsxBar;
import org.springframework.core.convert.converter.Converter;

import java.time.ZoneOffset;

public class DsxBarToBarConverter implements Converter<DsxBar, Bar> {
    @Override
    public Bar convert(DsxBar dsxBar) {
        return new Bar(
                new Asset("unknown"),
                dsxBar.getClose(),
                dsxBar.getTimestamp().toEpochSecond(ZoneOffset.UTC)
        );
    }
}
