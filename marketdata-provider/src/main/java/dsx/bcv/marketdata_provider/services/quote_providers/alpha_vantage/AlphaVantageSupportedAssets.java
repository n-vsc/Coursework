package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage;

import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models.AlphaVantageAsset;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class AlphaVantageSupportedAssets {

    private final AlphaVantageCsvParser alphaVantageCsvParser;

    @Getter
    private Set<AlphaVantageAsset> physicalCurrencies;
    @Getter
    private Set<AlphaVantageAsset> digitalCurrencies;
    @Getter
    private Set<AlphaVantageAsset> stocks;

    public AlphaVantageSupportedAssets(AlphaVantageCsvParser alphaVantageCsvParser) {

        this.alphaVantageCsvParser = alphaVantageCsvParser;

        physicalCurrencies = new HashSet<>(getAssetsFromFile("physical_currency_list.csv"));
        digitalCurrencies = new HashSet<>(getAssetsFromFile("digital_currency_list.csv"));
        stocks = new HashSet<>(getAssetsFromFile("stock_list.csv"));

        log.info("AlphaVantageSupportedAssets:\n" +
                "Physical currencies: {}\n" +
                "Digital currencies: {}\n" +
                "Stocks: {}",
                physicalCurrencies,
                digitalCurrencies,
                stocks);
    }

    private List<AlphaVantageAsset> getAssetsFromFile(String fileName) {

        var classLoader = this.getClass().getClassLoader();
        var inputStream = classLoader.getResourceAsStream(fileName);
        assert inputStream != null;
        var inputStreamReader = new InputStreamReader(inputStream);

        return alphaVantageCsvParser.parseAssets(inputStreamReader, ',');
    }
}
