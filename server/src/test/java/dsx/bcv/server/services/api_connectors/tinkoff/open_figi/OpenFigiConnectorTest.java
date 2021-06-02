package dsx.bcv.server.services.api_connectors.tinkoff.open_figi;

import dsx.bcv.server.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class OpenFigiConnectorTest {

    @Autowired
    private OpenFigiConnector openFigiConnector;

    @Test
    public void convertFigisToTickers() {

        List<String> tickers = openFigiConnector.convertFigisToTickers(List.of("BBG004S681B4", "BBG005DXDPK9"));

        assertThat(tickers.get(0), is("nlmk"));
        assertThat(tickers.get(1), is("fxgd"));
    }
}
