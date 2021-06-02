package dsx.bcv.server.services.csv_parsers;

import dsx.bcv.server.data.models.Trade;
import dsx.bcv.server.data.models.Transaction;
import dsx.bcv.server.services.csv_parsers.data_formats.MarketplaceDataFormat;
import dsx.bcv.server.services.data_services.TradeService;
import dsx.bcv.server.services.data_services.TransactionService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Парсит данные о сделках и транзакциях из Reader в типизированные списки
 */
@Component
public class CsvParser implements Parser {

    private MarketplaceDataFormat marketplaceDataFormat;
    private TradeService tradeService;
    private TransactionService transactionService;

    public CsvParser(
            @Qualifier("dsx_data_format") MarketplaceDataFormat marketplaceDataFormat,
            TradeService tradeService,
            TransactionService transactionService
    ) {
        this.marketplaceDataFormat = marketplaceDataFormat;
        this.tradeService = tradeService;
        this.transactionService = transactionService;
    }

    @Override
    public List<Trade> parseTrades(Reader inputReader, char separator) throws IOException {

        Iterable<CSVRecord> records = CSVFormat.newFormat(separator).parse(inputReader);
        var tradeFormat = marketplaceDataFormat.getTradeFormat();

        List<Trade> trades = new ArrayList<>();
        for (CSVRecord record : records) {
            var trade = tradeService.tradeOf(
                    record.get(tradeFormat.get(TradeField.DateTime)),
                    record.get(tradeFormat.get(TradeField.Instrument)),
                    record.get(tradeFormat.get(TradeField.TradeType)),
                    record.get(tradeFormat.get(TradeField.TradedQuantity)),
                    record.get(tradeFormat.get(TradeField.TradedQuantityCurrency)),
                    record.get(tradeFormat.get(TradeField.TradedPrice)),
                    record.get(tradeFormat.get(TradeField.TradedPriceCurrency)),
                    record.get(tradeFormat.get(TradeField.Commission)),
                    record.get(tradeFormat.get(TradeField.CommissionCurrency)),
                    record.get(tradeFormat.get(TradeField.TradeValueId))
            );
            trades.add(trade);
        }

        return trades;
    }

    @Override
    public List<Transaction> parseTransactions(Reader inputReader, char separator) throws IOException {

        Iterable<CSVRecord> records = CSVFormat.newFormat(separator).parse(inputReader);
        var transactionFormat = marketplaceDataFormat.getTransactionFormat();

        List<Transaction> transactions = new ArrayList<>();
        for (CSVRecord record : records) {
            var transaction = transactionService.transactionOf(
                    record.get(transactionFormat.get(TransactionField.DateTime)),
                    record.get(transactionFormat.get(TransactionField.TransactionType)),
                    record.get(transactionFormat.get(TransactionField.Currency)),
                    record.get(transactionFormat.get(TransactionField.Amount)),
                    record.get(transactionFormat.get(TransactionField.Commission)),
                    record.get(transactionFormat.get(TransactionField.TransactionStatus)),
                    record.get(transactionFormat.get(TransactionField.TransactionValueId))
            );
            transactions.add(transaction);
        }

        return transactions;
    }
}
