package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models.AlphaVantageAsset;
import org.springframework.core.convert.converter.Converter;

public class AlphaVantageAssetToAssetConverter
        implements Converter<AlphaVantageAsset, Asset> {
    @Override
    public Asset convert(AlphaVantageAsset source) {
        return new Asset(
                source.getCode(),
                source.getName()
        );
    }
}
