package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlphaVantageInstrument {

    public AlphaVantageAsset baseAsset;
    public AlphaVantageAsset quotedAsset;

    @Override
    public String toString() {
        return baseAsset + "-" + quotedAsset;
    }
}
