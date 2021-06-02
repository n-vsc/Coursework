package dsx.bcv.server.services.api_connectors.tinkoff.models;

import lombok.Data;

import java.util.List;

@Data
public class InstrumentsPayload {
    private List<TinkoffInstrument> instruments;
    private int total;
}
