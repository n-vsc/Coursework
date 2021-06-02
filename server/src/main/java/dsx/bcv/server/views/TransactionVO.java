package dsx.bcv.server.views;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dsx.bcv.server.data.models.TransactionStatus;
import dsx.bcv.server.data.models.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionVO {
    private long id;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateTime;
    private TransactionType transactionType;
    @JsonSerialize(using = ToStringSerializer.class)
    private CurrencyVO currency;
    private BigDecimal amount;
    private BigDecimal commission;
    private TransactionStatus transactionStatus;
    private String transactionValueId;

    @JsonCreator
    public TransactionVO(
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("transactionType") TransactionType transactionType,
            @JsonProperty("currency") String currency,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("commission") BigDecimal commission,
            @JsonProperty("transactionStatus") TransactionStatus transactionStatus,
            @JsonProperty("transactionValueId") String transactionValueId
    ) {
        this.dateTime = dateTime;
        this.transactionType = transactionType;
        this.currency = new CurrencyVO(currency);
        this.amount = amount;
        this.commission = commission;
        this.transactionStatus = transactionStatus;
        this.transactionValueId = transactionValueId;
    }

    public TransactionVO(
            long id,
            LocalDateTime dateTime,
            TransactionType transactionType,
            CurrencyVO currency,
            BigDecimal amount,
            BigDecimal commission,
            TransactionStatus transactionStatus,
            String transactionValueId
    ) {
        this.id = id;
        this.dateTime = dateTime;
        this.transactionType = transactionType;
        this.currency = currency;
        this.amount = amount;
        this.commission = commission;
        this.transactionStatus = transactionStatus;
        this.transactionValueId = transactionValueId;
    }
}
