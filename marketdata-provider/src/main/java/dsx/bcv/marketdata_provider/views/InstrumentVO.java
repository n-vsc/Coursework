package dsx.bcv.marketdata_provider.views;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
@JsonSerialize(using = ToStringSerializer.class)
public class InstrumentVO {

    private String assetPair;

    public InstrumentVO (String baseAsset, String quotedAsset) {
        assetPair = baseAsset + "-" + quotedAsset;
    }

    @Override
    public String toString() {
        return assetPair;
    }
}
