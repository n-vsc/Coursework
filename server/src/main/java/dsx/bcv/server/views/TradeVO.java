package dsx.bcv.server.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dsx.bcv.server.data.models.TradeType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TradeVO {
    private long id;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;
    @JsonSerialize(using = ToStringSerializer.class)
    private InstrumentVO instrument;
    private TradeType tradeType;
    private BigDecimal tradedQuantity;
    @JsonSerialize(using = ToStringSerializer.class)
    private CurrencyVO tradedQuantityCurrency;
    private BigDecimal tradedPrice;
    @JsonSerialize(using = ToStringSerializer.class)
    private CurrencyVO tradedPriceCurrency;
    private BigDecimal commission;
    @JsonSerialize(using = ToStringSerializer.class)
    private CurrencyVO commissionCurrency;
    private String tradeValueId;

    @JsonCreator
    public TradeVO(
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("instrument") String instrument,
            @JsonProperty("tradeType") TradeType tradeType,
            @JsonProperty("tradedQuantity") BigDecimal tradedQuantity,
            @JsonProperty("tradedQuantityCurrency") String tradedQuantityCurrency,
            @JsonProperty("tradedPrice") BigDecimal tradedPrice,
            @JsonProperty("tradedPriceCurrency") String tradedPriceCurrency,
            @JsonProperty("commission") BigDecimal commission,
            @JsonProperty("commissionCurrency") String commissionCurrency,
            @JsonProperty("tradeValueId") String tradeValueId
    ) {
        this.id = 0;
        this.dateTime = dateTime;
        this.instrument = new InstrumentVO(instrument);
        this.tradeType = tradeType;
        this.tradedQuantity = tradedQuantity;
        this.tradedQuantityCurrency = new CurrencyVO(tradedQuantityCurrency);
        this.tradedPrice = tradedPrice;
        this.tradedPriceCurrency = new CurrencyVO(tradedPriceCurrency);
        this.commission = commission;
        this.commissionCurrency = new CurrencyVO(commissionCurrency);
        this.tradeValueId = tradeValueId;
    }

    public TradeVO(
            long id,
            LocalDateTime dateTime,
            InstrumentVO instrument,
            TradeType tradeType,
            BigDecimal tradedQuantity,
            CurrencyVO tradedQuantityCurrency,
            BigDecimal tradedPrice,
            CurrencyVO tradedPriceCurrency,
            BigDecimal commission,
            CurrencyVO commissionCurrency,
            String tradeValueId
    ) {
        this.id = id;
        this.dateTime = dateTime;
        this.instrument = instrument;
        this.tradeType = tradeType;
        this.tradedQuantity = tradedQuantity;
        this.tradedQuantityCurrency = tradedQuantityCurrency;
        this.tradedPrice = tradedPrice;
        this.tradedPriceCurrency = tradedPriceCurrency;
        this.commission = commission;
        this.commissionCurrency = commissionCurrency;
        this.tradeValueId = tradeValueId;
    }
}
