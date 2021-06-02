package dsx.bcv.marketdata_provider.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TickerVO {
    private BigDecimal exchangeRate;
}
