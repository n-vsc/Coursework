package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Currency;
import dsx.bcv.server.views.CurrencyVO;
import org.springframework.core.convert.converter.Converter;

public class CurrencyVOToCurrencyConverter implements Converter<CurrencyVO, Currency> {
    @Override
    public Currency convert(CurrencyVO source) {
        return new Currency(
                source.getCode()
        );
    }
}
