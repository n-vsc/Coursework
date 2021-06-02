package dsx.bcv.marketdata_provider.services;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.data.repositories.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Прослойка между Контроллером и репозиторием
 */
@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public Asset save(Asset asset) {
        return assetRepository.save(asset);
    }

    public Optional<Asset> findByCode(String code) {
        return assetRepository.findByCode(code);
    }

    public Iterable<Asset> saveAll(Iterable<Asset> currencies) {
        return assetRepository.saveAll(currencies);
    }

    public Iterable<Asset> findAll() {
        return assetRepository.findAll();
    }

    public long count() {
        return assetRepository.count();
    }
}
