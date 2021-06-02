package dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DsxCurrencyVertex {
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
