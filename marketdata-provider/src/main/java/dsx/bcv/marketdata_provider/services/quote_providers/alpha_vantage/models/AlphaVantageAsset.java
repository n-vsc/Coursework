package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models;

import lombok.Data;

@Data
public class AlphaVantageAsset {

    private String code;
    private String name;

    public AlphaVantageAsset(String code, String name) {
        this.code = code.toLowerCase();
        this.name = name.toLowerCase();
    }

    @Override
    public String toString() {
        return code;
    }
}
