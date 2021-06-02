package dsx.bcv.server.data.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "trades")
@Data
@NoArgsConstructor
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Дата обмена
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;

    /**
     * Инструмент = пара активов: обмениваемый и запрашиваемый
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ManyToOne
    private Instrument instrument;

    /**
     * Тип обмена
     */
    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    /**
     * Объем запрашиваемого актива
     */
    private BigDecimal tradedQuantity;

    /**
     * Запрашиваемый актив
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ManyToOne
    private Currency tradedQuantityCurrency;

    /**
     * Цена запрашиваемого актива
     */
    private BigDecimal tradedPrice;

    /**
     * Валюта, в которой отображается цена запрашиваемого актива
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ManyToOne
    private Currency tradedPriceCurrency;

    /**
     * Объем комиссии
     */
    private BigDecimal commission;

    /**
     * Валюта, в которой отображается объем комиссии
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ManyToOne
    private Currency commissionCurrency;

    /**
     * Уникальный идентификатор обмена, предоставляемый торговой площадки
     */
    private String tradeValueId;

    public Trade(
            LocalDateTime dateTime,
            Instrument instrument,
            TradeType tradeType,
            BigDecimal tradedQuantity,
            Currency tradedQuantityCurrency,
            BigDecimal tradedPrice,
            Currency tradedPriceCurrency,
            BigDecimal commission,
            Currency commissionCurrency,
            String tradeValueId
    ) {
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
