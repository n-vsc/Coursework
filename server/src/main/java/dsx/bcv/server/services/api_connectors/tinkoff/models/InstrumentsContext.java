package dsx.bcv.server.services.api_connectors.tinkoff.models;

import lombok.Data;

@Data
public class InstrumentsContext {
    private String trackingId;
    private InstrumentsPayload payload;
    private String status;
}
