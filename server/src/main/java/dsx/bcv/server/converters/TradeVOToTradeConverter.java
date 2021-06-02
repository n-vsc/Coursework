package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Trade;
import dsx.bcv.server.views.TradeVO;
import org.springframework.core.convert.converter.Converter;

public class TradeVOToTradeConverter implements Converter<TradeVO, Trade> {
    @Override
    public Trade convert(TradeVO source) {
        return new Trade(
                source.getDateTime(),
                new InstrumentVOToInstrumentConverter().convert(source.getInstrument()),
                source.getTradeType(),
                source.getTradedQuantity(),
                new CurrencyVOToCurrencyConverter().convert(source.getTradedQuantityCurrency()),
                source.getTradedPrice(),
                new CurrencyVOToCurrencyConverter().convert(source.getTradedPriceCurrency()),
                source.getCommission(),
                new CurrencyVOToCurrencyConverter().convert(source.getCommissionCurrency()),
                source.getTradeValueId()
        );
    }
}
