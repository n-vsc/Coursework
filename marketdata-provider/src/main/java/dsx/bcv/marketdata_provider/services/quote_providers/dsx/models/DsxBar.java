package dsx.bcv.marketdata_provider.services.quote_providers.dsx.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DsxBar {

    private LocalDateTime timestamp;
    private BigDecimal close;

    @JsonCreator
    public DsxBar(
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("close") BigDecimal close
    ) {
        this.timestamp = timestamp;
        this.close = close;
    }
}
