package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.views.AssetVO;
import org.springframework.core.convert.converter.Converter;

public class AssetToAssetVOConverter implements Converter<Asset, AssetVO> {
    @Override
    public AssetVO convert(Asset source) {
        return new AssetVO(
                source.getCode(),
                source.getName()
        );
    }
}
