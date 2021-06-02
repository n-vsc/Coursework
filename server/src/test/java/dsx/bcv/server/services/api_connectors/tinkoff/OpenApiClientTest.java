package dsx.bcv.server.services.api_connectors.tinkoff;

import dsx.bcv.server.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.OffsetDateTime;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class OpenApiClientTest {

    @Autowired
    private OpenApiClient openApiClient;

    @Test
    public void getOperations() {
        var token = "";
        var operations = openApiClient.getOperations(token, OffsetDateTime.now().minusYears(1), OffsetDateTime.now());

        System.out.println(operations);
    }

    @Test
    public void getInstruments() {
        var token = "";
        var instruments = openApiClient.getInstruments(token);

        System.out.println(instruments);
    }

    @Test
    public void getTickerByFigi() {
        var token = "";
        var ticker = openApiClient.getTickerByFigi("BBG005DXDPK9", token);

        System.out.println(ticker);
    }
}