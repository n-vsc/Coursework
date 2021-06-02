package dsx.bcv.server.services.api_connectors.tinkoff.models;

import lombok.Data;

@Data
public class OperationsContext {
    private String trackingId;
    private OperationsPayload payload;
    private String status;
}
