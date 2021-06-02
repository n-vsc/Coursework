package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Instrument;
import dsx.bcv.server.views.InstrumentVO;
import org.springframework.core.convert.converter.Converter;

public class InstrumentVOToInstrumentConverter implements Converter<InstrumentVO, Instrument> {
    @Override
    public Instrument convert(InstrumentVO source) {
        return new Instrument(
                new CurrencyVOToCurrencyConverter().convert(source.getBaseCurrency()),
                new CurrencyVOToCurrencyConverter().convert(source.getQuotedCurrency())
        );
    }
}
