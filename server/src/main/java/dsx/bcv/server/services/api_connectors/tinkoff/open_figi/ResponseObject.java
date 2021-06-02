package dsx.bcv.server.services.api_connectors.tinkoff.open_figi;

import lombok.Data;

import java.util.List;

@Data
public class ResponseObject {
    public List<TickerInfo> data;
}
