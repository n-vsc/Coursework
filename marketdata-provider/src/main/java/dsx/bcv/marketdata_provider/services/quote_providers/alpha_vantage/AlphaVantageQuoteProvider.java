package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage;

import com.fasterxml.jackson.databind.ObjectMapper;
import dsx.bcv.marketdata_provider.data.models.Bar;
import dsx.bcv.marketdata_provider.services.RequestService;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlphaVantageQuoteProvider {

    private final RequestService requestService;
    private final ObjectMapper objectMapper;
    private final ConversionService conversionService;
    private final AlphaVantageApiKeyProvider alphaVantageApiKeyProvider;
    private final AlphaVantageRateLimiterService rateLimiterService;

    public AlphaVantageQuoteProvider(RequestService requestService, ObjectMapper objectMapper, ConversionService conversionService, AlphaVantageApiKeyProvider alphaVantageApiKeyProvider, AlphaVantageRateLimiterService rateLimiterService) {
        this.requestService = requestService;
        this.objectMapper = objectMapper;
        this.conversionService = conversionService;
        this.alphaVantageApiKeyProvider = alphaVantageApiKeyProvider;
        this.rateLimiterService = rateLimiterService;
    }

    @SneakyThrows
    public List<Bar> getForexDailyHistoricalRate(AlphaVantageAsset baseAsset) {

        log.trace("Historical rate for {} method called", baseAsset);

        var requestUrl =
                "https://www.alphavantage.co/query" +
                "?function=FX_DAILY" +
                "&from_symbol=" + baseAsset +
                "&to_symbol=USD" +
                "&outputsize=full" +
                "&apikey=" + alphaVantageApiKeyProvider.getApiKey();

        log.trace("Send request to Alpha Vantage, url: {}", requestUrl);

        rateLimiterService.getRateLimiter().acquire();
        var responseBody = requestService.doGetRequest(requestUrl);

        if (responseBody.contains("Error Message")) {
            log.warn("{} is not supported? Retry request...", baseAsset);
            return getForexDailyHistoricalRate(baseAsset);
        }
        if (responseBody.contains("Our standard API call frequency is 5 calls per minute and 500 calls per day")) {
            log.warn("ERROR. High API call frequency");
        }

        log.trace("Alpha Vantage returns:\n{}...", responseBody.substring(0, 200));

        var responseBodyJO = new JSONObject(responseBody);
        var rates = String.valueOf(responseBodyJO.get("Time Series FX (Daily)"));

        var ratesJO = new JSONObject(rates);
        var sortedKeySet = new TreeSet<>(ratesJO.keySet());

        var firstDate = sortedKeySet.first();
        var previousAlphaVantageForexBar = objectMapper.readValue(
                String.valueOf(ratesJO.get(firstDate)),
                AlphaVantageForexBar.class
        );
        previousAlphaVantageForexBar.setDate(LocalDate.parse(firstDate));

        var resultList = new ArrayList<AlphaVantageForexBar>();
        for (var key : sortedKeySet) {
            var assetRateString = String.valueOf(ratesJO.get(key));
            var currentAlphaVantageForexBar = objectMapper.readValue(
                    assetRateString,
                    AlphaVantageForexBar.class
            );
            currentAlphaVantageForexBar.setAsset(baseAsset);
            currentAlphaVantageForexBar.setDate(LocalDate.parse(key));
            for (var epochDay = previousAlphaVantageForexBar.getDate().toEpochDay() + 1;
                 epochDay < currentAlphaVantageForexBar.getDate().toEpochDay();
                 epochDay++) {
                resultList.add(
                        new AlphaVantageForexBar(
                                baseAsset,
                                previousAlphaVantageForexBar.getExchangeRate(),
                                LocalDate.ofEpochDay(epochDay)
                        )
                );
            }
            resultList.add(currentAlphaVantageForexBar);
            previousAlphaVantageForexBar = currentAlphaVantageForexBar;
        }

        log.trace("First 5 bars: {}", resultList.subList(0, 5));

        return resultList.stream()
                .map(bar -> conversionService.convert(bar, Bar.class))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Bar> getDigitalDailyHistoricalRate(AlphaVantageAsset baseAsset) {

        log.trace("Historical rate for {} method called", baseAsset);

        var requestUrl =
                "https://www.alphavantage.co/query" +
                "?function=DIGITAL_CURRENCY_DAILY" +
                "&symbol=" + baseAsset +
                "&market=USD" +
                "&apikey=" + alphaVantageApiKeyProvider.getApiKey();

        log.trace("Send request to Alpha Vantage, url: {}", requestUrl);

        rateLimiterService.getRateLimiter().acquire();
        var responseBody = requestService.doGetRequest(requestUrl);

        if (responseBody.contains("Error Message")) {
            log.warn("{} is not supported", baseAsset);
            return getDigitalDailyHistoricalRate(baseAsset);
        }
        if (responseBody.contains("Our standard API call frequency is 5 calls per minute and 500 calls per day")) {
            log.warn("ERROR. High API call frequency");
        }

        log.trace("Alpha Vantage returns:\n{}...", responseBody.substring(0, 200));

        var responseBodyJO = new JSONObject(responseBody);
        var rates = String.valueOf(responseBodyJO.get("Time Series (Digital Currency Daily)"));

        var ratesJO = new JSONObject(rates);
        var sortedKeySet = new TreeSet<>(ratesJO.keySet());

        var firstDate = sortedKeySet.first();
        var previousAlphaVantageCryptoBar = objectMapper.readValue(
                String.valueOf(ratesJO.get(firstDate)),
                AlphaVantageCryptoBar.class
        );
        previousAlphaVantageCryptoBar.setDate(LocalDate.parse(firstDate));

        var resultList = new ArrayList<AlphaVantageCryptoBar>();
        for (var key : sortedKeySet) {
            var assetRateString = String.valueOf(ratesJO.get(key));
            var currentAlphaVantageCryptoBar = objectMapper.readValue(
                    assetRateString,
                    AlphaVantageCryptoBar.class
            );
            currentAlphaVantageCryptoBar.setAsset(baseAsset);
            currentAlphaVantageCryptoBar.setDate(LocalDate.parse(key));
            for (var epochDay = previousAlphaVantageCryptoBar.getDate().toEpochDay() + 1;
                 epochDay < currentAlphaVantageCryptoBar.getDate().toEpochDay();
                 epochDay++) {
                resultList.add(
                        new AlphaVantageCryptoBar(
                                baseAsset,
                                previousAlphaVantageCryptoBar.getExchangeRate(),
                                LocalDate.ofEpochDay(epochDay)
                        )
                );
            }
            resultList.add(currentAlphaVantageCryptoBar);
            previousAlphaVantageCryptoBar = currentAlphaVantageCryptoBar;
        }

        log.trace("First 5 bars: {}", resultList.subList(0, 5));

        return resultList.stream()
                .map(rate -> conversionService.convert(rate, Bar.class))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Bar> getStockDailyHistoricalRate(AlphaVantageAsset baseAsset) {

        log.trace("Historical rate for {} method called", baseAsset);

        var requestUrl =
                "https://www.alphavantage.co/query" +
                        "?function=TIME_SERIES_DAILY" +
                        "&symbol=" + baseAsset +
                        "&outputsize=full" +
                        "&apikey=" + alphaVantageApiKeyProvider.getApiKey();

        log.trace("Send request to Alpha Vantage, url: {}", requestUrl);

        rateLimiterService.getRateLimiter().acquire();
        var responseBody = requestService.doGetRequest(requestUrl);

        if (responseBody.contains("Error Message")) {
            log.warn("{} is not supported? Retry request...", baseAsset);
            return getStockDailyHistoricalRate(baseAsset);
        }
        if (responseBody.contains("Our standard API call frequency is 5 calls per minute and 500 calls per day")) {
            log.warn("ERROR. High API call frequency");
        }

        log.trace("Alpha Vantage returns:\n{}...", responseBody.substring(0, 200));

        var responseBodyJO = new JSONObject(responseBody);
        var rates = String.valueOf(responseBodyJO.get("Time Series (Daily)"));

        var ratesJO = new JSONObject(rates);
        var sortedKeySet = new TreeSet<>(ratesJO.keySet());

        var firstDate = sortedKeySet.first();
        var previousAlphaVantageStockBar = objectMapper.readValue(
                String.valueOf(ratesJO.get(firstDate)),
                AlphaVantageStockBar.class
        );
        previousAlphaVantageStockBar.setDate(LocalDate.parse(firstDate));

        var resultList = new ArrayList<AlphaVantageStockBar>();
        for (var key : sortedKeySet) {
            var assetRateString = String.valueOf(ratesJO.get(key));
            var currentAlphaVantageStockBar = objectMapper.readValue(
                    assetRateString,
                    AlphaVantageStockBar.class
            );
            currentAlphaVantageStockBar.setAsset(baseAsset);
            currentAlphaVantageStockBar.setDate(LocalDate.parse(key));
            for (var epochDay = previousAlphaVantageStockBar.getDate().toEpochDay() + 1;
                 epochDay < currentAlphaVantageStockBar.getDate().toEpochDay();
                 epochDay++) {
                resultList.add(
                        new AlphaVantageStockBar(
                                baseAsset,
                                previousAlphaVantageStockBar.getExchangeRate(),
                                LocalDate.ofEpochDay(epochDay)
                        )
                );
            }
            resultList.add(currentAlphaVantageStockBar);
            previousAlphaVantageStockBar = currentAlphaVantageStockBar;
        }

        log.trace("First 5 bars: {}", resultList.subList(0, 5));

        return resultList.stream()
                .map(bar -> conversionService.convert(bar, Bar.class))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public AlphaVantageTicker getTicker(String baseAssetCode, String quotedAssetCode) {

        log.trace("Rate for {}-{} method called", baseAssetCode, quotedAssetCode);

        var requestUrl =
                "https://www.alphavantage.co/query" +
                        "?function=CURRENCY_EXCHANGE_RATE" +
                        "&from_currency=" + baseAssetCode +
                        "&to_currency=" + quotedAssetCode +
                        "&apikey=" + alphaVantageApiKeyProvider.getApiKey();

        log.trace("Send request to Alpha Vantage, url: {}", requestUrl);

        rateLimiterService.getRateLimiter().acquire();
        var responseBody = requestService.doGetRequest(requestUrl);
        if (responseBody.contains("Error Message")) {
            log.warn("Error messsage");
        }
        if (responseBody.contains(
                "Our standard API call frequency is 5 calls per minute and 500 calls per day"
        )) {
            log.warn("Error. High API call frequency");
            throw new RuntimeException(
                    "Error. High API call frequency. " +
                    "Our standard ticker API call frequency is 5 calls per minute and 500 calls per day."
            );
        }

        var responseBodyJO = new JSONObject(responseBody);
        var alphaVantageTickerString = String.valueOf(responseBodyJO.get("Realtime Currency Exchange Rate"));

        return objectMapper.readValue(alphaVantageTickerString, AlphaVantageTicker.class);
    }
}
