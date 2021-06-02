package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Bar;
import dsx.bcv.marketdata_provider.views.BarVO;
import org.springframework.core.convert.converter.Converter;

public class BarToBarVOConverter implements Converter<Bar, BarVO> {
    @Override
    public BarVO convert(Bar bar) {
        return new BarVO(
                bar.getExchangeRate(),
                bar.getTimestamp()
        );
    }
}
