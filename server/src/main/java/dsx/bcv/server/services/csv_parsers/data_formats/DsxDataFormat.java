package dsx.bcv.server.services.csv_parsers.data_formats;

import dsx.bcv.server.services.csv_parsers.TradeField;
import dsx.bcv.server.services.csv_parsers.TransactionField;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Формат (последовательность полей) csv файлов Dsx Global для сделок и транзакций
 */
@Component("dsx_data_format")
public class DsxDataFormat implements MarketplaceDataFormat {

    private final Map<TradeField, Integer> tradeFormat = Map.of(
            TradeField.DateTime, 0,
            TradeField.Instrument, 1,
            TradeField.TradeType, 2,
            TradeField.TradedQuantity, 3,
            TradeField.TradedQuantityCurrency, 4,
            TradeField.TradedPrice, 5,
            TradeField.TradedPriceCurrency, 6,
            TradeField.Commission, 9,
            TradeField.CommissionCurrency, 10,
            TradeField.TradeValueId, 12
    );

    private final Map<TransactionField, Integer> transactionFormat = Map.of(
            TransactionField.DateTime, 0,
            TransactionField.TransactionType, 1,
            TransactionField.Currency, 2,
            TransactionField.Amount, 3,
            TransactionField.Commission, 4,
            TransactionField.TransactionStatus, 7,
            TransactionField.TransactionValueId, 9
    );

    @Override
    public Map<TradeField, Integer> getTradeFormat() {
        return tradeFormat;
    }

    @Override
    public Map<TransactionField, Integer> getTransactionFormat() {
        return transactionFormat;
    }
}
