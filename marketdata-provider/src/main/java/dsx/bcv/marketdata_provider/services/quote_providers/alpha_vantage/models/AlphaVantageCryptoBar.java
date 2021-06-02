package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AlphaVantageCryptoBar {

    private AlphaVantageAsset asset;
    private BigDecimal exchangeRate;
    private LocalDate date;

    @JsonCreator
    public AlphaVantageCryptoBar(
            @JsonProperty("4a. close (USD)") BigDecimal exchangeRate
    ) {
        this.exchangeRate = exchangeRate;
    }
}
