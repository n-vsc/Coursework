package dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph;

import dsx.bcv.marketdata_provider.Application;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.DsxSupportedInstruments;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class DsxCurrencyGraphTest {

    @Autowired
    private DsxCurrencyGraph dsxCurrencyGraph;
    @Autowired
    private DsxSupportedInstruments dsxSupportedInstruments;

    @Test
    public void getShortestPathFromEurToRub() {
        final var shortestPath = dsxCurrencyGraph.getShortestPath(
                new DsxCurrencyVertex("eur"),
                new DsxCurrencyVertex("rub")
        );

        assertArrayEquals(
                List.of(
                        new DsxInstrumentEdge(
                                new DsxCurrencyVertex("eur"),
                                new DsxCurrencyVertex("usd"),
                                false
                        ),
                        new DsxInstrumentEdge(
                                new DsxCurrencyVertex("usd"),
                                new DsxCurrencyVertex("rub"),
                                false
                        )
                ).toArray(),
                shortestPath.toArray()
        );
    }

    @Test
    public void getShortestPathFromRubToEur() {
        final var shortestPath = dsxCurrencyGraph.getShortestPath(
                new DsxCurrencyVertex("rub"),
                new DsxCurrencyVertex("eur")
        );

        assertArrayEquals(
                List.of(
                        new DsxInstrumentEdge(
                                new DsxCurrencyVertex("usd"),
                                new DsxCurrencyVertex("rub"),
                                true
                        ),
                        new DsxInstrumentEdge(
                                new DsxCurrencyVertex("eur"),
                                new DsxCurrencyVertex("usd"),
                                true
                        )
                ).toArray(),
                shortestPath.toArray()
        );
    }
}