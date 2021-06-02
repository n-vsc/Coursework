package dsx.bcv.server.services.data_services;

import dsx.bcv.server.data.models.Currency;
import dsx.bcv.server.data.models.Transaction;
import dsx.bcv.server.data.models.TransactionStatus;
import dsx.bcv.server.data.models.TransactionType;
import dsx.bcv.server.data.repositories.TransactionRepository;
import dsx.bcv.server.exceptions.NotFoundException;
import dsx.bcv.server.services.LocalDateTimeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CurrencyService currencyService;
    private final LocalDateTimeService localDateTimeService;

    public TransactionService(
            TransactionRepository transactionRepository,
            CurrencyService currencyService,
            LocalDateTimeService localDateTimeService
    ) {
        this.transactionRepository = transactionRepository;
        this.currencyService = currencyService;
        this.localDateTimeService = localDateTimeService;
    }

    public Transaction save(Transaction transaction) {
        transaction.setCurrency(currencyService.save(transaction.getCurrency()));
        return transactionRepository.save(transaction);
    }

    public List<Transaction> saveAll(List<Transaction> transactions) {
        transactions.forEach(this::save);
        return transactions;
    }

    public Transaction transactionOf(
        String localDateTime,
        String transactionType,
        String currency,
        String amount,
        String commission,
        String transactionStatus,
        String transactionValueId
    ) {
        return new Transaction(
                localDateTimeService.getDateTimeFromString(localDateTime),
                transactionType.toLowerCase().equals("deposit") ? TransactionType.Deposit : TransactionType.Withdraw,
                new Currency(currency),
                new BigDecimal(amount.replaceAll(",", ".")),
                new BigDecimal(commission.replaceAll(",", ".")),
                transactionStatus.toLowerCase().equals("complete") ?
                        TransactionStatus.Complete : TransactionStatus.Complete,
                transactionValueId
        );
    }

    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(long id) {
        return transactionRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Transaction updateById(long id, Transaction transaction) {
        transactionRepository.deleteById(id);
        assert transaction != null;
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteById(long id) {
        if (transactionRepository.existsById(id))
            transactionRepository.deleteById(id);
        else
            throw new NotFoundException();
    }

    public long count() {
        return transactionRepository.count();
    }
}
