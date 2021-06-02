package dsx.bcv.server.views;

import lombok.Data;

@Data
public class CurrencyVO {

    private String code;

    public CurrencyVO(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
