package dsx.bcv.server.services.api_connectors.tinkoff.models;

import lombok.Data;

@Data
public class TinkoffInstrument {
    private String figi;
    private String ticker;
    private String isin;
    private String minPriceIncrement;
    private String lot;
    private String currency;
    private String name;
    private String type;
    private String faceValue;
}
