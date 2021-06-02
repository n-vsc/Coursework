package dsx.bcv.server.services.api_connectors.tinkoff;

import dsx.bcv.server.data.models.Trade;
import dsx.bcv.server.data.models.Transaction;
import dsx.bcv.server.services.api_connectors.ApiConnector;
import org.springframework.stereotype.Service;
import ru.tinkoff.invest.openapi.models.operations.OperationType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TinkoffApiConnector implements ApiConnector {

    private final TinkoffOperationsConverter tinkoffOperationsConverter;
    private final OpenApiClient openApiClient;

    public TinkoffApiConnector(
            TinkoffOperationsConverter tinkoffOperationsConverter,
            OpenApiClient openApiClient
    ) {
        this.tinkoffOperationsConverter = tinkoffOperationsConverter;
        this.openApiClient = openApiClient;
    }

    @Override
    public List<Trade> getAllTrades(String token) {

        var operations = openApiClient.getOperations(token, OffsetDateTime.MIN, OffsetDateTime.MAX);

        return operations.stream()
                .filter(
                        operation -> operation.operationType == OperationType.Buy ||
                                operation.operationType == OperationType.BuyCard ||
                                operation.operationType == OperationType.Sell
                )
                .map(operation -> tinkoffOperationsConverter.convertOperationToTrade(operation, token))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAllTransactions(String token) {

        var operations = openApiClient.getOperations(token, OffsetDateTime.MIN, OffsetDateTime.MAX);

        return operations.stream()
                .filter(
                        operation -> !(operation.operationType == OperationType.Buy ||
                                operation.operationType == OperationType.BuyCard ||
                                operation.operationType == OperationType.Sell ||
                                operation.operationType == OperationType.BrokerCommission)
                )
                .map(tinkoffOperationsConverter::convertOperationToTransaction)
                .collect(Collectors.toList());
    }
}
