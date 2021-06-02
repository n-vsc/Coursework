package dsx.bcv.server.views;

import lombok.Data;

@Data
public class PortfolioVO {

    private long id;
    private String name;

    public PortfolioVO(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
