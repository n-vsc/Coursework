package dsx.bcv.server.services.api_connectors;

import dsx.bcv.server.data.models.Trade;
import dsx.bcv.server.data.models.Transaction;

import java.util.List;

/**
 * Общий интерфейс для всех коннекторов к торговым площадкам
 */
public interface ApiConnector {
    List<Trade> getAllTrades(String token);
    List<Transaction> getAllTransactions(String token);
}
