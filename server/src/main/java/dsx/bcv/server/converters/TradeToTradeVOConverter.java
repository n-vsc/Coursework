package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Trade;
import dsx.bcv.server.views.TradeVO;
import org.springframework.core.convert.converter.Converter;

public class TradeToTradeVOConverter implements Converter<Trade, TradeVO> {
    @Override
    public TradeVO convert(Trade source) {
        return new TradeVO(
                source.getId(),
                source.getDateTime(),
                new InstrumentToInstrumentVOConverter().convert(source.getInstrument()),
                source.getTradeType(),
                source.getTradedQuantity(),
                new CurrencyToCurrencyVOConverter().convert(source.getTradedQuantityCurrency()),
                source.getTradedPrice(),
                new CurrencyToCurrencyVOConverter().convert(source.getTradedPriceCurrency()),
                source.getCommission(),
                new CurrencyToCurrencyVOConverter().convert(source.getCommissionCurrency()),
                source.getTradeValueId()
        );
    }
}
