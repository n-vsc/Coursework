package dsx.bcv.server.services.api_connectors.tinkoff;

import dsx.bcv.server.data.models.*;
import dsx.bcv.server.services.api_connectors.tinkoff.models.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.models.operations.OperationType;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Service
public class TinkoffOperationsConverter {

    private final OpenApiClient openApiClient;

    public TinkoffOperationsConverter(OpenApiClient openApiClient) {
        this.openApiClient = openApiClient;
    }

    public Trade convertOperationToTrade(Operation operation, String token) {

        if (!(operation.operationType == OperationType.Buy ||
                operation.operationType == OperationType.BuyCard ||
                operation.operationType == OperationType.Sell))
            throw new RuntimeException("");

        var ticker = openApiClient.getTickerByFigi(operation.figi, token);

        return new Trade(
                operation.date.toLocalDateTime(),
                new Instrument(
                        new Currency(operation.currency.name().toLowerCase()),
                        new Currency(ticker)
                ),
                operation.operationType == OperationType.Sell ?
                        TradeType.Sell : TradeType.Buy,
                new BigDecimal(Objects.requireNonNull(operation.quantity).toString()),
                new Currency(ticker),
                Objects.requireNonNull(operation.price).abs(),
                new Currency(operation.currency.name().toLowerCase()),
                operation.commission != null ?
                        Objects.requireNonNull(operation.commission).value.abs()
                        : new BigDecimal("0"),
                operation.commission != null ?
                        new Currency(operation.commission.currency.name().toLowerCase())
                        : new Currency("usd"),
                operation.id
        );
    }

    public Transaction convertOperationToTransaction(Operation operation) {

        if (operation.operationType == OperationType.Buy ||
                operation.operationType == OperationType.BuyCard ||
                operation.operationType == OperationType.Sell)
            throw new RuntimeException("");

        return new Transaction(
                operation.date.toLocalDateTime(),
                operation.payment.signum() == -1 ?
                        TransactionType.Withdraw
                        : TransactionType.Deposit,
                new Currency(operation.currency.name().toLowerCase()),
                operation.payment.abs(),
                operation.commission != null ?
                        Objects.requireNonNull(operation.commission).value.abs()
                        : new BigDecimal("0"),
                TransactionStatus.Complete,
                operation.id
        );
    }
}
