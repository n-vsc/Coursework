package dsx.bcv.server.services.csv_parsers;

import dsx.bcv.server.data.models.Trade;
import dsx.bcv.server.data.models.Transaction;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Component
public interface Parser {
    List<Trade> parseTrades(Reader inputReader, char separator) throws IOException;
    List<Transaction> parseTransactions(Reader inputReader, char separator) throws IOException;
}
