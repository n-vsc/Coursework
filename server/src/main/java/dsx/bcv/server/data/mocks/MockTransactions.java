package dsx.bcv.server.data.mocks;

import dsx.bcv.server.data.models.Transaction;
import dsx.bcv.server.services.csv_parsers.CsvParser;
import dsx.bcv.server.services.data_services.TransactionService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Добавляет mock транзакции пользователя в портфели для ручного тестирования
 */
@Component
public class MockTransactions {

    private MockTransactions(CsvParser csvParser, TransactionService transactionService) {

        var classLoader = this.getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream("dsx_transactions_dev.csv");
        assert inputStream != null;
        var inputStreamReader = new InputStreamReader(inputStream);

        List<Transaction> transactions = new ArrayList<>();
        try {
            transactions = csvParser.parseTransactions(
                    inputStreamReader, ';');
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (transactionService.count() == 0) {
            for (var transaction : transactions) {
                transactionService.save(transaction);
            }
        }
    }
}
