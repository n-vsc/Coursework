package dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph;

import dsx.bcv.marketdata_provider.services.quote_providers.dsx.DsxSupportedCurrencies;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.DsxSupportedInstruments;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DsxCurrencyGraph {

    private final Graph<DsxCurrencyVertex, DsxInstrumentEdge> currencyGraph = new SimpleDirectedGraph<>(DsxInstrumentEdge.class);

    private DsxSupportedCurrencies dsxSupportedCurrencies;
    private DsxSupportedInstruments dsxSupportedInstruments;

    public DsxCurrencyGraph(
            DsxSupportedCurrencies dsxSupportedCurrencies,
            DsxSupportedInstruments dsxSupportedInstruments)
    {
        this.dsxSupportedCurrencies = dsxSupportedCurrencies;
        this.dsxSupportedInstruments = dsxSupportedInstruments;

        fillGraph();
    }

    private void fillGraph() {

        dsxSupportedCurrencies.getCurrencies()
                .forEach(currencyGraph::addVertex);

        dsxSupportedInstruments.getInstruments()
                .forEach(dsxInstrumentEdge -> currencyGraph.addEdge(
                        dsxInstrumentEdge.getBaseCurrency(),
                        dsxInstrumentEdge.getQuotedCurrency(),
                        dsxInstrumentEdge
                ));

        dsxSupportedInstruments.getReversedInstruments()
                .forEach(dsxInstrumentEdge -> currencyGraph.addEdge(
                        dsxInstrumentEdge.getQuotedCurrency(),
                        dsxInstrumentEdge.getBaseCurrency(),
                        dsxInstrumentEdge
                ));
    }

    public List<DsxInstrumentEdge> getShortestPath(DsxCurrencyVertex v1, DsxCurrencyVertex v2) {
        var path = DijkstraShortestPath.findPathBetween(currencyGraph, v1, v2);
        return path.getEdgeList();
    }
}
