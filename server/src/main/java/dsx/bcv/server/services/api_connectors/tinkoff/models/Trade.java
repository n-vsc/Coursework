package dsx.bcv.server.services.api_connectors.tinkoff.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Модель сделки в рамках операции.
 */
public class Trade {

    /**
     * Идентификатор сделки.
     */
    @NotNull
    public final String tradeId;

    /**
     * Дата/время совершения сделки.
     */
    @NotNull
    public final OffsetDateTime date;

    /**
     * Цена за единицу.
     */
    @NotNull
    public final BigDecimal price;

    /**
     * Объём сделки.
     */
    public final int quantity;

    @JsonCreator
    public Trade(@JsonProperty(value = "tradeId", required = true)
                 @NotNull
                 final String tradeId,
                 @JsonProperty(value = "date", required = true)
                 @NotNull
                 final String date,
                 @JsonProperty(value = "price", required = true)
                 @NotNull
                 final BigDecimal price,
                 @JsonProperty(value = "quantity", required = true)
                 final int quantity) {
        this.tradeId = tradeId;
        this.date = OffsetDateTime.parse(date);
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Trade(" + "tradeId='" + tradeId + '\'' +
                ", date=" + date +
                ", price=" + price +
                ", quantity=" + quantity +
                ')';
    }
}
