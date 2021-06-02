package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Currency;
import dsx.bcv.server.views.CurrencyVO;
import org.springframework.core.convert.converter.Converter;

public class CurrencyToCurrencyVOConverter implements Converter<Currency, CurrencyVO> {
    @Override
    public CurrencyVO convert(Currency source) {
        return new CurrencyVO(
                source.getCode()
        );
    }
}
