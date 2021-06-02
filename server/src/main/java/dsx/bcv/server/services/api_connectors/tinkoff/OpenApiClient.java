package dsx.bcv.server.services.api_connectors.tinkoff;

import com.fasterxml.jackson.databind.ObjectMapper;
import dsx.bcv.server.services.api_connectors.tinkoff.models.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class OpenApiClient {

    private ObjectMapper objectMapper = new ObjectMapper();

    public String getTickerByFigi(String figi, String token) {

        final var url = "https://api-invest.tinkoff.ru/openapi/market/search/by-figi?figi=" + figi;
        var responseString = doGetRequest(url, token);

        InstrumentContext instrumentContext;
        try {
            instrumentContext = objectMapper.readValue(responseString, InstrumentContext.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return instrumentContext
                .getPayload()
                .getTicker()
                .toLowerCase();
    }

    public List<Operation> getOperations(String token, OffsetDateTime from, OffsetDateTime to) {

        final var url = "https://api-invest.tinkoff.ru/openapi/operations" +
                "?from=" + "2019-08-19T18%3A38%3A33.131642%2B03%3A00" +//from.toString() +
                "&to=" + "2020-08-19T18%3A38%3A33.131642%2B03%3A00";//to.toString();

        var responseString = doGetRequest(url, token);

        OperationsContext operationsContext;
        try {
            operationsContext = objectMapper.readValue(responseString, OperationsContext.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return operationsContext.getPayload().getOperations();
    }

    public List<TinkoffInstrument> getInstruments(String token) {

        final List<TinkoffInstrument> result;

        final var urlStocks = "https://api-invest.tinkoff.ru/openapi/market/stocks";
        var stocksResponseString = doGetRequest(urlStocks, token);
        InstrumentsContext stocksInstrumentsContext;
        try {
            stocksInstrumentsContext = objectMapper.readValue(stocksResponseString, InstrumentsContext.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        result = stocksInstrumentsContext.getPayload().getInstruments();

        final var urlCurrencies = "https://api-invest.tinkoff.ru/openapi/market/currencies";
        var currenciesResponseString = doGetRequest(urlCurrencies, token);
        InstrumentsContext currenciesInstrumentsContext;
        try {
            currenciesInstrumentsContext = objectMapper.readValue(currenciesResponseString, InstrumentsContext.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        result.addAll(currenciesInstrumentsContext.getPayload().getInstruments());

        return result;
    }

    private String doGetRequest(final String url, final String token) {

        var client = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

        final var tokenPrefix = "Bearer ";

        var request = new Request.Builder()
                .url(url)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", tokenPrefix + token)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String responseString;
        try {
            responseString = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return responseString;
    }
}
