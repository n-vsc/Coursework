package dsx.bcv.server.services.api_connectors.tinkoff.open_figi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenFigiRequestFormat {
    private String idType;
    private String idValue;
}
