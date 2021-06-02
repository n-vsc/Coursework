package dsx.bcv.marketdata_provider.data.repositories;

import dsx.bcv.marketdata_provider.data.models.Asset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {
    Optional<Asset> findByCode(String code);
}
