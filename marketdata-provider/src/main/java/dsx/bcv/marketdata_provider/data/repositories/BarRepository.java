package dsx.bcv.marketdata_provider.data.repositories;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.data.models.Bar;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BarRepository extends CrudRepository<Bar, Long> {
    Optional<Bar> findTopByBaseAsset(Asset asset);
    List<Bar> findByBaseAssetAndTimestampBetween(Asset asset, long from, long to);
    Optional<Bar> findTopByBaseAssetOrderByTimestampDesc(Asset asset);
}
