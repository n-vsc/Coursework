package dsx.bcv.server.converters;

import dsx.bcv.server.data.models.Transaction;
import dsx.bcv.server.views.TransactionVO;
import org.springframework.core.convert.converter.Converter;

public class TransactionToTransactionVOConverter implements Converter<Transaction, TransactionVO> {
    @Override
    public TransactionVO convert(Transaction source) {
        return new TransactionVO(
                source.getId(),
                source.getDateTime(),
                source.getTransactionType(),
                new CurrencyToCurrencyVOConverter().convert(source.getCurrency()),
                source.getAmount(),
                source.getCommission(),
                source.getTransactionStatus(),
                source.getTransactionValueId()
        );
    }
}
