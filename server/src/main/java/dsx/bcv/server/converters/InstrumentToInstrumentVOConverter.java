package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Instrument;
import dsx.bcv.server.views.InstrumentVO;
import org.springframework.core.convert.converter.Converter;

public class InstrumentToInstrumentVOConverter implements Converter<Instrument, InstrumentVO> {
    @Override
    public InstrumentVO convert(Instrument source) {
        return new InstrumentVO(
                new CurrencyToCurrencyVOConverter().convert(source.getBaseCurrency()),
                new CurrencyToCurrencyVOConverter().convert(source.getQuotedCurrency())
        );
    }
}
