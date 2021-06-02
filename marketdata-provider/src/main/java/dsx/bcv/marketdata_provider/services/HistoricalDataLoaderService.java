package dsx.bcv.marketdata_provider.services;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.AlphaVantageQuoteProvider;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.AlphaVantageSupportedAssets;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models.AlphaVantageAsset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Один раз при запуске программы загружает исторические данные о котировках активов, загруженных в таблицу asset
 */
@Service
@Slf4j
public class HistoricalDataLoaderService {

    private final AlphaVantageQuoteProvider alphaVantageQuoteProvider;
    private final ConversionService conversionService;
    private final AssetService assetService;
    private final AlphaVantageSupportedAssets alphaVantageSupportedAssets;
    private final BarService barService;
    private final ActualDataLoaderService actualDataLoaderService;

    public HistoricalDataLoaderService(
            AlphaVantageQuoteProvider alphaVantageQuoteProvider,
            ConversionService conversionService,
            AssetService assetService,
            AlphaVantageSupportedAssets alphaVantageSupportedAssets,
            BarService barService,
            ActualDataLoaderService actualDataLoaderService) {

        this.alphaVantageQuoteProvider = alphaVantageQuoteProvider;
        this.conversionService = conversionService;
        this.assetService = assetService;
        this.alphaVantageSupportedAssets = alphaVantageSupportedAssets;
        this.barService = barService;
        this.actualDataLoaderService = actualDataLoaderService;

//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(this::loadData);
//        executorService.shutdown();

        new Thread(this::loadData).start();
    }

    public void loadData() {

        log.info("Start loading historical data");

        saveSupportedAssetsToDb();
        loadPhysicalCurrencies();
        loadDigitalCurrencies();
        loadStocks();

        actualDataLoaderService.loadDataFromLastBars();
    }

    /**
     * Загружает активы, поддерживаемые Alpha Vantage, в таблицу assets
     */
    void saveSupportedAssetsToDb() {

        log.info("saveSupportedAssetsToDb method called");

        if (assetService.count() != 0) {
            log.info("Supported assets are already saved in database");
            return;
        }

        final List<Asset> physicalCurrencies = alphaVantageSupportedAssets.getPhysicalCurrencies()
                .stream()
                .map(currency -> conversionService.convert(currency, Asset.class))
                .collect(Collectors.toList());

        assetService.saveAll(physicalCurrencies);
        log.info("Physical currencies saved. List: {}", physicalCurrencies);

        final List<Asset> digitalCurrencies = alphaVantageSupportedAssets.getDigitalCurrencies()
                .stream()
                .map(currency -> conversionService.convert(currency, Asset.class))
                .collect(Collectors.toList());

        assetService.saveAll(digitalCurrencies);
        log.info("Digital currencies saved. List: {}", digitalCurrencies);

        final List<Asset> stocks = alphaVantageSupportedAssets.getStocks()
                .stream()
                .map(stock -> conversionService.convert(stock, Asset.class))
                .collect(Collectors.toList());

        assetService.saveAll(stocks);
        log.info("Stocks saved. List: {}", stocks);
    }

    /**
     * Загружает историю котировок физических валют в таблицу bars
     */
    private void loadPhysicalCurrencies() {

        final var physicalCurrencies = alphaVantageSupportedAssets.getPhysicalCurrencies()
                .stream()
                .map(currency -> conversionService.convert(currency, Asset.class))
                .collect(Collectors.toList());

        log.debug("Start loading physical currencies. List: {}", physicalCurrencies);

        for (int i = 0; i < physicalCurrencies.size(); i++) {

            var currency = physicalCurrencies.get(i);
            log.debug("Loading history for {} {}/{}", currency, i + 1, physicalCurrencies.size());

            if (!barService.existsByAsset(currency)) {
                log.debug("History for {} isn't saved yet", currency);
                final var forexDailyBars =
                        alphaVantageQuoteProvider.getForexDailyHistoricalRate(
                                conversionService.convert(currency, AlphaVantageAsset.class)
                        );
                barService.saveAll(forexDailyBars);
                log.debug("History for {} saved", currency);
            } else {
                log.debug("History for {} is already saved", currency);
            }
        }
        log.info("Physical currencies history saved");
    }

    /**
     * Загружает историю котировок криптовалют в таблицу bars
     */
    private void loadDigitalCurrencies() {

        final var digitalCurrencies = alphaVantageSupportedAssets.getDigitalCurrencies()
                .stream()
                .map(currency -> conversionService.convert(currency, Asset.class))
                .collect(Collectors.toList());

        log.debug("Start loading digital currencies. List: {}", digitalCurrencies);

        for (int i = 0; i < digitalCurrencies.size(); i++) {

            var currency = digitalCurrencies.get(i);
            log.debug("Loading history for {} {}/{}", currency, i + 1, digitalCurrencies.size());

            if (!barService.existsByAsset(currency)) {
                final var digitalDailyBars =
                        alphaVantageQuoteProvider.getDigitalDailyHistoricalRate(
                                conversionService.convert(currency, AlphaVantageAsset.class)
                        );
                barService.saveAll(digitalDailyBars);
                log.debug("History for {} saved", currency);
            } else {
                log.debug("History for {} is already saved", currency);
            }
        }
        log.info("Digital currencies history saved");
    }

    /**
     * Загружает историю котировок акций в таблицу bars
     */
    private void loadStocks() {

        final var stocks = alphaVantageSupportedAssets.getStocks()
                .stream()
                .map(stock -> conversionService.convert(stock, Asset.class))
                .collect(Collectors.toList());

        log.debug("Start loading stocks. List: {}", stocks);

        for (int i = 0; i < stocks.size(); i++) {

            var stock = stocks.get(i);
            log.debug("Loading history for {} {}/{}", stock, i + 1, stocks.size());

            if (!barService.existsByAsset(stock)) {
                final var stockDailyBars =
                        alphaVantageQuoteProvider.getStockDailyHistoricalRate(
                                conversionService.convert(stock, AlphaVantageAsset.class)
                        );
                barService.saveAll(stockDailyBars);
                log.debug("History for {} saved", stock);
            } else {
                log.debug("History for {} is already saved", stock);
            }
        }
        log.info("Stocks history saved");
    }
}
