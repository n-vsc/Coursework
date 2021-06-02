package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlphaVantageTicker {

    private AlphaVantageAsset baseAsset;
    private AlphaVantageAsset quotedAsset;
    private BigDecimal exchangeRate;

    @JsonCreator
    public AlphaVantageTicker(
            @JsonProperty("1. From_Currency Code") String baseCurrencyCode,
            @JsonProperty("2. From_Currency Name") String baseCurrencyName,
            @JsonProperty("3. To_Currency Code") String quotedCurrencyCode,
            @JsonProperty("4. To_Currency Name") String quotedCurrencyName,
            @JsonProperty("5. Exchange Rate") BigDecimal exchangeRate
    ) {
        baseAsset = new AlphaVantageAsset(baseCurrencyCode, baseCurrencyName);
        quotedAsset = new AlphaVantageAsset(quotedCurrencyCode, quotedCurrencyName);
        this.exchangeRate = exchangeRate;
    }
}
