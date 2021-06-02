package dsx.bcv.server.data.mocks;

import dsx.bcv.server.data.models.Trade;
import dsx.bcv.server.services.csv_parsers.CsvParser;
import dsx.bcv.server.services.data_services.TradeService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Добавляет mock сделки пользователя в портфели для ручного тестирования
 */
@Component
public class MockTrades {

    private MockTrades(CsvParser csvParser, TradeService tradeService) {

        var classLoader = this.getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream("dsx_trades_dev.csv");
        assert inputStream != null;
        var inputStreamReader = new InputStreamReader(inputStream);

        List<Trade> trades = new ArrayList<>();
        try {
            trades = csvParser.parseTrades(
                    inputStreamReader, ';');
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (tradeService.count() == 0) {
            for (var trade : trades) {
                tradeService.save(trade);
            }
        }
    }
}
