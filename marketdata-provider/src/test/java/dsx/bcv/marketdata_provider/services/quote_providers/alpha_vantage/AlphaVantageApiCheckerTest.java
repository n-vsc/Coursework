package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage;

import dsx.bcv.marketdata_provider.Application;
import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.services.RequestService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.Collectors;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class AlphaVantageApiCheckerTest {

    @Autowired
    private AlphaVantageSupportedAssets alphaVantageSupportedCurrencies;
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private AlphaVantageApiKeyProvider alphaVantageApiKeyProvider;
    @Autowired
    private AlphaVantageRateLimiterService rateLimiterService;

    @Test
    public void checkAll() {
        checkPhysicalCurrencies();
        checkDigitalCurrencies();
    }

    private void checkPhysicalCurrencies() {

        System.out.println("Checking supported physical currencies");

        final var physicalCurrencies = this.alphaVantageSupportedCurrencies.getPhysicalCurrencies()
                .stream()
                .map(currency -> conversionService.convert(currency, Asset.class))
                .collect(Collectors.toList());

        for (var currency : physicalCurrencies) {
            var requestUrl =
                    "https://www.alphavantage.co/query" +
                            "?function=FX_DAILY" +
                            "&from_symbol=" + currency +
                            "&to_symbol=USD" +
                            "&apikey=" + alphaVantageApiKeyProvider.getApiKey();
            sendRequest(currency, requestUrl);
        }
    }

    private void checkDigitalCurrencies() {

        System.out.println("Checking supported digital currencies");

        final var digitalCurrencies = this.alphaVantageSupportedCurrencies.getDigitalCurrencies()
                .stream()
                .map(currency -> conversionService.convert(currency, Asset.class))
                .collect(Collectors.toList());

        var i = 1;
        for (var currency : digitalCurrencies) {
            var requestUrl =
                    "https://www.alphavantage.co/query" +
                            "?function=DIGITAL_CURRENCY_DAILY" +
                            "&symbol=" + currency +
                            "&market=USD" +
                            "&apikey=" + alphaVantageApiKeyProvider.getApiKey();
            sendRequest(currency, requestUrl);
        }
    }

    private void sendRequest(Asset asset, String requestUrl) {
        rateLimiterService.getRateLimiter().acquire();
        var responseBody = requestService.doGetRequest(requestUrl);
        if (responseBody.contains("Error Message")) {
            System.out.println(asset);
        }
        if (responseBody.contains("Our standard API call frequency is 5 calls per minute and 500 calls per day")) {
            System.out.println("ERROR. High API call frequency");
        }
    }
}