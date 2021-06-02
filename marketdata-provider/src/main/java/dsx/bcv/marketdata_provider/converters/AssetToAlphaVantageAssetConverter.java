package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models.AlphaVantageAsset;
import org.springframework.core.convert.converter.Converter;

public class AssetToAlphaVantageAssetConverter
        implements Converter<Asset, AlphaVantageAsset> {
    @Override
    public AlphaVantageAsset convert(Asset source) {
        return new AlphaVantageAsset(
                source.getCode(),
                source.getName()
        );
    }
}
