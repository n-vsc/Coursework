package dsx.bcv.marketdata_provider.data.repositories;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.data.models.Ticker;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TickerRepository extends CrudRepository<Ticker, Long> {
    Optional<Ticker> findByBaseAsset(Asset baseAsset);
}
