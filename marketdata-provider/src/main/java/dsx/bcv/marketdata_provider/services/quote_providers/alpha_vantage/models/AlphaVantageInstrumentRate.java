package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlphaVantageInstrumentRate {

    private BigDecimal exchangeRate;

    @JsonCreator
    public AlphaVantageInstrumentRate(
            @JsonProperty("5. Exchange Rate") BigDecimal exchangeRate
    ) {
        this.exchangeRate = exchangeRate;
    }
}
