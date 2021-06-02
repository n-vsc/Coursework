package dsx.bcv.marketdata_provider.services.quote_providers.dsx.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class DsxTicker {

    private BigDecimal exchangeRate;

    @JsonCreator
    public DsxTicker(
            @JsonProperty("ask") BigDecimal ask,
            @JsonProperty("bid") BigDecimal bid
    ) {
        exchangeRate = ask.add(bid).divide(new BigDecimal("2"), 10, RoundingMode.HALF_UP);
    }
}


