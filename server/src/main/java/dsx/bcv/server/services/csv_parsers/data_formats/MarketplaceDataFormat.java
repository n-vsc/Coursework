package dsx.bcv.server.services.csv_parsers.data_formats;

import dsx.bcv.server.services.csv_parsers.TradeField;
import dsx.bcv.server.services.csv_parsers.TransactionField;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface MarketplaceDataFormat {
    Map<TradeField, Integer> getTradeFormat();
    Map<TransactionField, Integer> getTransactionFormat();
}
