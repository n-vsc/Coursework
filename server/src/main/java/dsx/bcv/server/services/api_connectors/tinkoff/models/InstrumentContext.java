package dsx.bcv.server.services.api_connectors.tinkoff.models;

import lombok.Data;

@Data
public class InstrumentContext {
    private String trackingId;
    private TinkoffInstrument payload;
    private String status;
}
