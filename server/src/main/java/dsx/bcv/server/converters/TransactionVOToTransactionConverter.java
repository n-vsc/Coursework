package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Transaction;
import dsx.bcv.server.views.TransactionVO;
import org.springframework.core.convert.converter.Converter;

public class TransactionVOToTransactionConverter implements Converter<TransactionVO, Transaction> {
    @Override
    public Transaction convert(TransactionVO source) {
        return new Transaction(
                source.getDateTime(),
                source.getTransactionType(),
                new CurrencyVOToCurrencyConverter().convert(source.getCurrency()),
                source.getAmount(),
                source.getCommission(),
                source.getTransactionStatus(),
                source.getTransactionValueId()
        );
    }
}
