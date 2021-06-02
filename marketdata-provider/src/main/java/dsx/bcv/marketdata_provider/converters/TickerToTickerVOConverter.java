package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Ticker;
import dsx.bcv.marketdata_provider.views.TickerVO;
import org.springframework.core.convert.converter.Converter;

public class TickerToTickerVOConverter implements Converter<Ticker, TickerVO> {
    @Override
    public TickerVO convert(Ticker ticker) {
        return new TickerVO(
                ticker.getExchangeRate()
        );
    }
}
