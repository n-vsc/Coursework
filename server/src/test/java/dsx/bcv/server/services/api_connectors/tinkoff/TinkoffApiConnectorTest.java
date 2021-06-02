package dsx.bcv.server.services.api_connectors.tinkoff;

import dsx.bcv.server.Application;
import dsx.bcv.server.services.api_connectors.tinkoff.models.Operation;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.tinkoff.invest.openapi.models.Currency;
import ru.tinkoff.invest.openapi.models.MoneyAmount;
import ru.tinkoff.invest.openapi.models.operations.OperationStatus;
import ru.tinkoff.invest.openapi.models.operations.OperationType;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TinkoffApiConnectorTest {

    @Autowired
    private OpenApiClient openApiClient;

    @Test
    public void getAllTrades() {

        var openApiClientMock = mock(OpenApiClient.class);
        when(openApiClientMock.getOperations(any(), any(), any())).thenReturn(operations);
        var tinkoffApiConnector = new TinkoffApiConnector(
                new TinkoffOperationsConverter(openApiClient),
                openApiClientMock
        );

        var token = "";
        var trades = tinkoffApiConnector.getAllTrades(token);

        System.out.println(trades);
    }

    @Test
    public void getAllTransactions() {

        var openApiClientMock = mock(OpenApiClient.class);
        when(openApiClientMock.getOperations(any(), any(), any())).thenReturn(operations);
        var tinkoffApiConnector = new TinkoffApiConnector(
                new TinkoffOperationsConverter(openApiClient),
                openApiClientMock
        );

        var token = "";
        var transactions = tinkoffApiConnector.getAllTransactions(token);

        System.out.println(transactions);
    }

    private List<Operation> operations = List.of(
            new Operation(
                    "id",
                    OperationStatus.Done,
                    null,
                    null,
                    Currency.RUB,
                    new BigDecimal("-51.18"),
                    null,
                    null,
                    null,
                    null,
                    false,
                    "2019-11-29T19:52:53+03:00",
                    OperationType.BrokerCommission
            ),
            new Operation(
                    "id",
                    OperationStatus.Done,
                    null,
                    null,
                    Currency.RUB,
                    new BigDecimal("15151.18"),
                    null,
                    null,
                    null,
                    null,
                    false,
                    "2019-11-29T19:52:52+03:00",
                    OperationType.PayIn
            ),
            new Operation(
                    "id",
                    OperationStatus.Done,
                    null,
                    new MoneyAmount(
                            Currency.USD,
                            new BigDecimal("-0.74")
                    ),
                    Currency.USD,
                    new BigDecimal("1473.6"),
                    new BigDecimal("15.35"),
                    96,
                    "BBG000C46HM9",
                    null,
                    false,
                    "2019-11-29T19:50:12.605905+03:00",
                    OperationType.Sell
            ),
            new Operation(
                    "id",
                    OperationStatus.Done,
                    null,
                    new MoneyAmount(
                            Currency.RUB,
                            new BigDecimal("-73.4")
                    ),
                    Currency.RUB,
                    new BigDecimal("-146790"),
                    new BigDecimal("8.155"),
                    18000,
                    "BBG004S68BR5",
                    null,
                    false,
                    "2019-11-18T13:38:13.793064+03:00",
                    OperationType.BuyCard
            )
    );
}