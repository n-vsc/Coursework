package dsx.bcv.marketdata_provider.services.quote_providers.dsx;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.data.models.Ticker;
import dsx.bcv.marketdata_provider.exceptions.NotFoundException;
import dsx.bcv.marketdata_provider.services.RequestService;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph.DsxCurrencyGraph;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph.DsxCurrencyVertex;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph.DsxInstrumentEdge;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.models.DsxBar;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.models.DsxTicker;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DsxQuoteProvider {

    private RequestService requestService;
    private ObjectMapper objectMapper;
    private ConversionService conversionService;
    private DsxCurrencyGraph dsxCurrencyGraph;
    private DsxSupportedCurrencies dsxSupportedCurrencies;
    private DsxSupportedInstruments dsxSupportedInstruments;

    public DsxQuoteProvider(
            RequestService requestService,
            ObjectMapper objectMapper,
            ConversionService conversionService,
            DsxCurrencyGraph dsxCurrencyGraph,
            DsxSupportedCurrencies dsxSupportedCurrencies, DsxSupportedInstruments dsxSupportedInstruments)
    {
        this.requestService = requestService;
        this.objectMapper = objectMapper;
        this.conversionService = conversionService;
        this.dsxCurrencyGraph = dsxCurrencyGraph;
        this.dsxSupportedCurrencies = dsxSupportedCurrencies;
        this.dsxSupportedInstruments = dsxSupportedInstruments;
    }

    public List<DsxBar> getBarsInPeriod(String instrument, long startTime, long endTime) {

        var currencyPair = getCurrencyPairFromString(instrument);
        var instrumentList = dsxCurrencyGraph.getShortestPath(
                currencyPair.getFirst(),
                currencyPair.getSecond()
        );

        var barsList = new ArrayList<List<DsxBar>>();
        for (var dsxInstrumentEdge : instrumentList) {
            String responseBody;
            responseBody = requestService.doGetRequest(
                    String.format(
                            "https://api.dsxglobal.com/api/2/public/candles/%s?period=D1&from=%s&till=%s&limit=1000",
                            dsxInstrumentEdge.toString(),
                            LocalDateTime.ofEpochSecond(startTime, 0, ZoneOffset.UTC),
                            LocalDateTime.ofEpochSecond(endTime, 0, ZoneOffset.UTC)
                    ));
            List<DsxBar> tmp;
            try {
                tmp = objectMapper.readValue(responseBody, new TypeReference<List<DsxBar>>() {});
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            barsList.add(tmp);
        }

        for (var bars : barsList) {
            if (bars.size() != barsList.get(0).size()) {
                log.warn("Not enough data on server");
                throw new RuntimeException("Not enough data on server");
            }
        }

        var resultList = new ArrayList<DsxBar>();
        for (int i = 0; i < barsList.get(0).size(); i++) {
            var exchangeRate = new BigDecimal("1");
            for (int j = 0; j < barsList.size(); j++) {
                    if (instrumentList.get(j).isReversed()) {
                        exchangeRate = exchangeRate.divide(
                                barsList.get(j).get(i).getClose(),
                                10,
                                RoundingMode.HALF_UP
                        );
                    } else {
                        exchangeRate = exchangeRate.multiply(barsList.get(j).get(i).getClose());
                    }
            }
            resultList.add(new DsxBar(
                    barsList.get(0).get(i).getTimestamp(),
                    exchangeRate
            ));
        }

        return resultList;
    }

    public Ticker getTicker(String instrument) {

        var currencyPair = getCurrencyPairFromString(instrument);
        var instrumentEdgeList = dsxCurrencyGraph.getShortestPath(
                currencyPair.getFirst(),
                currencyPair.getSecond()
        );

        var tickerList = new ArrayList<Ticker>();
        for (var instrumentEdge : instrumentEdgeList) {
            String responseBody;
            responseBody = requestService.doGetRequest(
                    String.format(
                            "https://api.dsxglobal.com/api/2/public/ticker/%s",
                            instrumentEdge.toString()
                    ));
            var jsonObject = new JSONObject(responseBody);
            var tickerString = String.valueOf(jsonObject.get(fixBchProblem(instrumentEdge.toString())));
            DsxTicker dsxTicker;
            try {
                dsxTicker = objectMapper.readValue(tickerString, DsxTicker.class);
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
                throw new RuntimeException(e);
            }
            tickerList.add(conversionService.convert(dsxTicker, Ticker.class));
        }

        var exchangeRate = new BigDecimal("1");
        for (int j = 0; j < tickerList.size(); j++) {
            if (instrumentEdgeList.get(j).isReversed()) {
                exchangeRate = exchangeRate.divide(
                        tickerList.get(j).getExchangeRate(),
                        10,
                        RoundingMode.HALF_UP
                );
            } else {
                exchangeRate = exchangeRate.multiply(tickerList.get(j).getExchangeRate());
            }
        }

        return new Ticker(
                new Asset(currencyPair.getFirst().getName()),
                exchangeRate,
                0
        );
    }

    public List<DsxInstrumentEdge> getInstruments() {
        return new ArrayList<>(dsxSupportedInstruments.getInstruments());
    }

    private Pair<DsxCurrencyVertex, DsxCurrencyVertex> getCurrencyPairFromString(String instrument) {

        var currencyPair = Lists.newArrayList(Splitter.on("-").split(instrument));
        if (currencyPair.size() != 2) {
            throw new NotFoundException("Invalid instrument");
        }
        var baseCurrency = new DsxCurrencyVertex(currencyPair.get(0));
        var quotedCurrency = new DsxCurrencyVertex(currencyPair.get(1));
        if (!dsxSupportedCurrencies.getCurrencies().contains(baseCurrency)) {
            log.warn("Base currency {} from request is not supported by dsx", baseCurrency);
            throw new NotFoundException(
                    "Base currency" + baseCurrency + "from request is not supported by dsx"
            );
        }
        if (!dsxSupportedCurrencies.getCurrencies().contains(quotedCurrency)) {
            log.warn("Quoted currency {} from request is not supported by dsx", quotedCurrency);
            throw new NotFoundException(
                    "Quoted currency" + quotedCurrency + "from request is not supported by dsx"
            );
        }

        return new Pair<>(baseCurrency, quotedCurrency);
    }

    private String fixBchProblem(String instrument) {
        return instrument.replace("bch", "bcc");
    }
}
