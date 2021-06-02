package dsx.bcv.marketdata_provider.services;

import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.data.models.Ticker;
import dsx.bcv.marketdata_provider.data.repositories.TickerRepository;
import dsx.bcv.marketdata_provider.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TickerService {

    private final TickerRepository tickerRepository;

    public TickerService(TickerRepository tickerRepository) {
        this.tickerRepository = tickerRepository;
    }

    public Ticker save(Ticker ticker) {
        return tickerRepository.save(ticker);
    }

    public Iterable<Ticker> saveAll(Iterable<Ticker> tickers) {
        return tickerRepository.saveAll(tickers);
    }

    public Ticker findByBaseAsset(Asset baseAsset) {
        return tickerRepository.findByBaseAsset(baseAsset)
                .orElseThrow(NotFoundException::new);
    }

    public void deleteAll() {
        tickerRepository.deleteAll();
    }

    public long count() {
        return tickerRepository.count();
    }

    public boolean existsByBaseAsset(Asset baseAsset) {
        return tickerRepository.findByBaseAsset(baseAsset).isPresent();
    }

    public void updateExchangeRateByBaseAsset(BigDecimal exchangeRate, Asset baseAsset) {
        var ticker = findByBaseAsset(baseAsset);
        ticker.setExchangeRate(exchangeRate);
        save(ticker);
    }
}
