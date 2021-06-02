package dsx.bcv.marketdata_provider.converters;

import dsx.bcv.marketdata_provider.data.models.Instrument;
import dsx.bcv.marketdata_provider.views.InstrumentVO;
import org.springframework.core.convert.converter.Converter;

public class InstrumentToInstrumentVOConverter implements Converter<Instrument, InstrumentVO> {
    @Override
    public InstrumentVO convert(Instrument instrument) {
        return new InstrumentVO(
                instrument.baseAsset.toString(),
                instrument.quotedAsset.toString()
        );
    }
}
