package dsx.bcv.marketdata_provider.services;

import dsx.bcv.marketdata_provider.data.models.Ticker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Обновляет данные о текущих курсах активов в таблице tickers
 */
@Service
@Slf4j
public class ActualDataLoaderService {

    private final TickerService tickerService;
    private final BarService barService;
    private final AssetService assetService;

    public ActualDataLoaderService(
            TickerService tickerService,
            BarService barService,
            AssetService assetService
    ) {
        this.tickerService = tickerService;
        this.barService = barService;
        this.assetService = assetService;
    }

    /**
     * Раз в день в 03:00 обновляет поле exchangeRate в элементах таблицы tickers
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void loadDataFromLastBars() {

        log.info("Loading tickers from last bars");

        final var assets = assetService.findAll();
        for (var asset : assets) {
            final var lastBar = barService.findTopByBaseAssetOrderByTimestampDesc(asset);
            final var ticker = new Ticker(
                    lastBar.getBaseAsset(),
                    lastBar.getExchangeRate(),
                    lastBar.getTimestamp()
            );
            if (!tickerService.existsByBaseAsset(ticker.getBaseAsset())) {
                tickerService.save(ticker);
            }
            else {
                tickerService.updateExchangeRateByBaseAsset(ticker.getExchangeRate(), ticker.getBaseAsset());
            }
            log.debug(
                    "Ticker for {} saved. Rate: {}. Timestamp: {}",
                    asset,
                    ticker.getExchangeRate(),
                    ticker.getTimestamp()
            );
        }
        log.info("Loading tickers from last bars is complete");
    }
}
