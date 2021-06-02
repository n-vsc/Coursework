package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Portfolio;
import dsx.bcv.server.views.PortfolioVO;
import org.springframework.core.convert.converter.Converter;

public class PortfolioToPortfolioVOConverter implements Converter<Portfolio, PortfolioVO> {
    @Override
    public PortfolioVO convert(Portfolio source) {
        return new PortfolioVO(
                source.getId(),
                source.getName()
//                source.getTrades().stream()
//                        .map(trade -> new TradeToTradeVOConverter().convert(trade))
//                        .collect(Collectors.toSet()),
//                source.getTransactions().stream()
//                        .map(transaction -> new TransactionToTransactionVOConverter().convert(transaction))
//                        .collect(Collectors.toSet())
        );
    }
}
