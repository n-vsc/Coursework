package dsx.bcv.marketdata_provider.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Instrument {

    /**
     * Обмениваемый актив
     */
    public Asset baseAsset;

    /**
     * Запрашиваемый актив
     */
    public Asset quotedAsset;

    @Override
    public String toString() {
        return baseAsset + "-" + quotedAsset;
    }
}
