package dsx.bcv.server.services.data_services;

import dsx.bcv.server.data.models.Currency;
import dsx.bcv.server.data.models.Instrument;
import dsx.bcv.server.data.models.Trade;
import dsx.bcv.server.data.models.TradeType;
import dsx.bcv.server.data.repositories.TradeRepository;
import dsx.bcv.server.exceptions.NotFoundException;
import dsx.bcv.server.services.LocalDateTimeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final InstrumentService instrumentService;
    private final CurrencyService currencyService;
    private final LocalDateTimeService localDateTimeService;

    public TradeService(
            TradeRepository tradeRepository,
            InstrumentService instrumentService,
            CurrencyService currencyService,
            LocalDateTimeService localDateTimeService
    ) {
        this.tradeRepository = tradeRepository;
        this.instrumentService = instrumentService;
        this.currencyService = currencyService;
        this.localDateTimeService = localDateTimeService;
    }

    public Trade save(Trade trade) {
        trade.setInstrument(instrumentService.save(trade.getInstrument()));
        trade.setTradedQuantityCurrency(currencyService.save(trade.getTradedQuantityCurrency()));
        trade.setTradedPriceCurrency(currencyService.save(trade.getTradedPriceCurrency()));
        trade.setCommissionCurrency(currencyService.save(trade.getCommissionCurrency()));
        return tradeRepository.save(trade);
    }

    public List<Trade> saveAll(List<Trade> trades) {
        trades.forEach(this::save);
        return trades;
    }

    public Trade tradeOf(
            String localDateTime,
            String instrument,
            String tradeType,
            String tradedQuantity,
            String tradedQuantityCurrency,
            String tradedPrice,
            String tradedPriceCurrency,
            String commission,
            String commissionCurrency,
            String tradeValueId
    ) {
        return new Trade(
                localDateTimeService.getDateTimeFromString(localDateTime),
                new Instrument(instrument),
                tradeType.toLowerCase().equals("buy") ? TradeType.Buy : TradeType.Sell,
                new BigDecimal(tradedQuantity.replaceAll(",", ".")),
                new Currency(tradedQuantityCurrency),
                new BigDecimal(tradedPrice.replaceAll(",", ".")),
                new Currency(tradedPriceCurrency),
                new BigDecimal(commission.replaceAll(",", ".")),
                new Currency(commissionCurrency),
                tradeValueId
        );
    }

    public Iterable<Trade> findAll() {
        return tradeRepository.findAll();
    }

    public Trade findById(long id) {
        return tradeRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void deleteById(long id) {
        if (tradeRepository.existsById(id))
            tradeRepository.deleteById(id);
        else
            throw new NotFoundException();
    }

    @Transactional
    public Trade updateById(long id, Trade trade) {
        tradeRepository.deleteById(id);
        assert trade != null;
        return tradeRepository.save(trade);
    }

    public long count() {
        return tradeRepository.count();
    }
}
