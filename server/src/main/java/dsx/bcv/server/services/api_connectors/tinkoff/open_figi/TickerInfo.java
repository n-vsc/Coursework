package dsx.bcv.server.services.api_connectors.tinkoff.open_figi;

import lombok.Data;

@Data
public class TickerInfo {
    private String figi;
    private String name;
    private String ticker;
    private String exchCode;
    private String compositeFIGI;
    private String uniqueID;
    private String securityType;
    private String marketSector;
    private String shareClassFIGI;
    private String uniqueIDFutOpt;
    private String securityType2;
    private String securityDescription;
}
