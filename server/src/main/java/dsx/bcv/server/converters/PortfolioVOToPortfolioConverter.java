package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Portfolio;
import dsx.bcv.server.views.PortfolioVO;
import org.springframework.core.convert.converter.Converter;

public class PortfolioVOToPortfolioConverter implements Converter<PortfolioVO, Portfolio> {
    @Override
    public Portfolio convert(PortfolioVO source) {
        return new Portfolio(
                source.getName()
        );
    }
}
