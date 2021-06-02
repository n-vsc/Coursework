package dsx.bcv.server.services.data_services;

import dsx.bcv.server.data.models.Portfolio;
import dsx.bcv.server.data.models.Trade;
import dsx.bcv.server.data.models.Transaction;
import dsx.bcv.server.data.repositories.PortfolioRepository;
import dsx.bcv.server.exceptions.NotFoundException;
import dsx.bcv.server.services.api_connectors.ApiConnectorName;
import dsx.bcv.server.services.api_connectors.ApiConnectorRepository;
import dsx.bcv.server.services.csv_parsers.CsvParser;
import dsx.bcv.server.services.csv_parsers.data_formats.CsvFileFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Позволяет делать CRUD операции с портфелями пользователей и хранящимися в них сделками и транзакциями.
 */
@Service
@Slf4j
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final TradeService tradeService;
    private final TransactionService transactionService;
    private final CsvParser csvParser;
    private final ApiConnectorRepository apiConnectorRepository;

    public PortfolioService(
            PortfolioRepository portfolioRepository,
            TradeService tradeService,
            TransactionService transactionService,
            CsvParser csvParser,
            ApiConnectorRepository apiConnectorRepository) {
        this.portfolioRepository = portfolioRepository;
        this.tradeService = tradeService;
        this.transactionService = transactionService;
        this.csvParser = csvParser;
        this.apiConnectorRepository = apiConnectorRepository;
    }

    public Portfolio save(Portfolio portfolio) {
        var savedPortfolio = portfolioRepository.save(portfolio);
        log.debug("save: Portfolio {} saved", savedPortfolio);
        return savedPortfolio;
    }

    public Portfolio findById(long id) {
        var portfolio = portfolioRepository.findById(id).orElseThrow(NotFoundException::new);
        log.debug("findById: Portfolio {} found", portfolio);
        return portfolio;
    }

    public Portfolio findByName(String name) {
        var portfolio = portfolioRepository.findByName(name).orElseThrow(NotFoundException::new);
        log.debug("findByName: Portfolio {} found", portfolio);
        return portfolio;
    }

    /**
     * @param portfolioId id портфеля
     * @return список сделок в портфеле
     */
    public List<Trade> getTrades(long portfolioId) {
        var portfolio = findById(portfolioId);
        var trades = new ArrayList<>(portfolio.getTrades());
        log.debug("getTrades: Trades {}... found", trades.stream().limit(2));
        return trades;
    }

    /**
     * @param portfolioId id портфеля
     * @return список транзакций в портфеле
     */
    public List<Transaction> getTransactions(long portfolioId) {
        var portfolio = findById(portfolioId);
        var transactions = new ArrayList<>(portfolio.getTransactions());
        log.debug("getTransactions: Transactions {}... found", transactions.stream().limit(2));
        return transactions;
    }

    public Iterable<Portfolio> findAll() {
        log.debug("findAll: Called");
        return portfolioRepository.findAll();
    }

    public Portfolio updateById(long id, Portfolio portfolio) {
        log.debug("updateById: Called (Not implemented)");
        throw new RuntimeException("Portfolio updating is not implemented");
    }

    /**
     * Загружает данные о сделках пользователя из csv файла в портфель
     * @param file файл с данными
     * @param csvFileFormat имя торговой площадки, которая задает формат данных
     * @param portfolioId id портфеля, куда загружать данные
     */
    @SneakyThrows
    public void uploadTradeFile(MultipartFile file, CsvFileFormat csvFileFormat, long portfolioId) {
        log.debug(
                "uploadTradeFile: File with filename {}({}) uploaded",
                file.getName(),
                file.getOriginalFilename()
        );
        var trades = csvParser.parseTrades(new InputStreamReader(file.getInputStream()), ';');
        log.debug("uploadTradeFile: Trades {}... parsed", trades.stream().limit(2));
        addTrades(trades, portfolioId);
    }

    /**
     * Загружает данные о транзакциях пользователя из csv файла в портфель
     * @param file файл с данными
     * @param csvFileFormat имя торговой площадки, которая задает формат данных
     * @param portfolioId id портфеля, куда загружать данные
     */
    @SneakyThrows
    public void uploadTransactionFile(MultipartFile file, CsvFileFormat csvFileFormat, long portfolioId) {
        log.debug(
                "uploadTransactionFile: File with filename {}({}) uploaded",
                file.getName(),
                file.getOriginalFilename()
        );
        var transactions = csvParser.parseTransactions(new InputStreamReader(file.getInputStream()), ';');
        log.debug("uploadTransactionFile: Transactions {}... parsed", transactions.stream().limit(2));
        addTransactions(transactions, portfolioId);
    }

    /**
     * Загружает данные о сделках пользователя из внешнего аккаунта в портфель
     * @param apiConnectorName имя торговой площадки, откуда загружать данные
     * @param token токен пользователя от аккаунта, откуда загружать данные
     * @param portfolioId id портфеля, в который загружать данные
     */
    public void uploadTradesUsingApi(ApiConnectorName apiConnectorName, String token, long portfolioId) {
        log.debug("uploadTradesUsingApi: called");
        var apiConnector = apiConnectorRepository.getApiConnectorByName(apiConnectorName);
        var trades = apiConnector.getAllTrades(token);
        addTrades(trades, portfolioId);
    }

    /**
     * Загружает данные о транзакциях пользователя из внешнего аккаунта в портфель
     * @param apiConnectorName имя торговой площадки, откуда загружать данные
     * @param token токен пользователя от аккаунта, откуда загружать данные
     * @param portfolioId id портфеля, в который загружать данные
     */
    public void uploadTransactionsUsingApi(ApiConnectorName apiConnectorName, String token, long portfolioId) {
        log.debug("uploadTransactionsUsingApi: called");
        var apiConnector = apiConnectorRepository.getApiConnectorByName(apiConnectorName);
        var transactions = apiConnector.getAllTransactions(token);
        addTransactions(transactions, portfolioId);
    }

    /**
     * Добавляет сделки в портфель пользователя
     * @param trades список сделок для добавления
     * @param portfolioId id портфеля, куда загружать сделки
     */
    public void addTrades(List<Trade> trades, long portfolioId) {
        var savedTrades = tradeService.saveAll(trades);
        var portfolio = findById(portfolioId);
        portfolio.getTrades().addAll(savedTrades);
        save(portfolio);
        log.debug("addTrades: Trades added");
    }

    /**
     * Добавляет транзакции в портфель пользователя
     * @param transactions список транзакций для добавления
     * @param portfolioId id портфеля, куда загружать транзакции
     */
    public void addTransactions(List<Transaction> transactions, long portfolioId) {
        var savedTransactions = transactionService.saveAll(transactions);
        var portfolio = findById(portfolioId);
        portfolio.getTransactions().addAll(savedTransactions);
        save(portfolio);
        log.debug("addTransactions: Transactions added");
    }

    public void deleteById(long id) {
        log.debug("deleteById: Called with id = {}", id);
        portfolioRepository.deleteById(id);
    }

    public long count() {
        log.debug("count: Called");
        return portfolioRepository.count();
    }

    public Trade findTradeById(long tradeId, long portfolioId) {
        var portfolio = findById(portfolioId);
        var trades = new ArrayList<>(portfolio.getTrades());
        return trades.stream()
                .filter(trade -> trade.getId() == tradeId)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    public Trade addTrade(Trade trade, long portfolioId) {
        var savedTrade = tradeService.save(trade);
        var portfolio = findById(portfolioId);
        portfolio.getTrades().add(savedTrade);
        save(portfolio);
        log.debug("addTrade: Trade with id {} successfully added", savedTrade.getId());
        return savedTrade;
    }

    public void deleteTradeById(long tradeId, long portfolioId) {
        var portfolio = findById(portfolioId);
        var trades = portfolio.getTrades();
        trades.removeIf(trade -> trade.getId() == tradeId);
        save(portfolio);
        log.debug("deleteTradeById: Trade with id {} successfully deleted", tradeId);
    }

    public Transaction findTransactionById(long transactionId, long portfolioId) {
        var portfolio = findById(portfolioId);
        var trades = new ArrayList<>(portfolio.getTransactions());
        return trades.stream()
                .filter(transaction -> transaction.getId() == transactionId)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    public Transaction addTransaction(Transaction transaction, long portfolioId) {
        var savedTransaction = transactionService.save(transaction);
        var portfolio = findById(portfolioId);
        portfolio.getTransactions().add(savedTransaction);
        save(portfolio);
        log.debug("addTransaction: Transaction with id {} successfully added", savedTransaction.getId());
        return savedTransaction;
    }

    public void deleteTransactionById(long transactionId, long portfolioId) {
        var portfolio = findById(portfolioId);
        var trades = portfolio.getTransactions();
        trades.removeIf(transaction -> transaction.getId() == transactionId);
        save(portfolio);
        log.debug("deleteTransactionById: Transaction with id {} successfully deleted", transactionId);
    }
}
