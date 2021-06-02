package dsx.bcv.marketdata_provider.services;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import dsx.bcv.marketdata_provider.data.models.Asset;
import dsx.bcv.marketdata_provider.data.models.Bar;
import dsx.bcv.marketdata_provider.data.models.Ticker;
import dsx.bcv.marketdata_provider.exceptions.NotFoundException;
import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.AlphaVantageSupportedAssets;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.DsxQuoteProvider;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuoteProviderService {

    private final DsxQuoteProvider dsxQuoteProvider;
    private final ConversionService conversionService;
    private final AlphaVantageSupportedAssets alphaVantageSupportedAssets;
    private final BarService barService;
    private final TickerService tickerService;
    private final AssetService assetService;

    public QuoteProviderService(
            DsxQuoteProvider dsxQuoteProvider,
            ConversionService conversionService,
            AlphaVantageSupportedAssets alphaVantageSupportedAssets,
            BarService barService,
            TickerService tickerService,
            AssetService assetService
    ) {
        this.dsxQuoteProvider = dsxQuoteProvider;
        this.conversionService = conversionService;
        this.alphaVantageSupportedAssets = alphaVantageSupportedAssets;
        this.barService = barService;
        this.tickerService = tickerService;
        this.assetService = assetService;
    }

    /**
     * @return список поддерживаемых активов
     */
    public List<Asset> getAssets() {
        var assets = alphaVantageSupportedAssets.getPhysicalCurrencies();
        assets.addAll(alphaVantageSupportedAssets.getDigitalCurrencies());
        assets.addAll(alphaVantageSupportedAssets.getStocks());
        return assets.stream()
                .map(alphaVantageCurrency ->
                        conversionService.convert(alphaVantageCurrency, Asset.class)
                )
                .collect(Collectors.toList());
    }

    /**
     * @param instrument пара тикеров, разделенных дефисом
     * @param startTime unit timestamp начала интервала
     * @param endTime unit timestamp конца интервала
     * @return ежедневные котировки по инструменту instrument в интервале между startTime и endTime
     */
    public List<Bar> getDailyBarsInPeriod(String instrument, long startTime, long endTime) {

        final var currencyPair = getCurrencyPairFromInstrumentString(instrument);
        final var baseCurrency = currencyPair.getFirst();
        final var quotedCurrency = currencyPair.getSecond();

        var baseCurrencyBars = barService.findByBaseAssetAndTimestampBetween(
                baseCurrency,
                startTime,
                endTime
        );

        var quotedCurrencyBars = barService.findByBaseAssetAndTimestampBetween(
                quotedCurrency,
                startTime,
                endTime
        );

        if (baseCurrencyBars.size() != quotedCurrencyBars.size()) {
            log.warn("Not enough data on server");
            throw new RuntimeException(
                    "Not enough data on server. Try to select a smaller interval."
            );
        }

        var resultBars = new ArrayList<Bar>();
        for (int i = 0; i < baseCurrencyBars.size(); i++) {
            if (baseCurrencyBars.get(i).getTimestamp() != quotedCurrencyBars.get(i).getTimestamp()) {
                log.warn("Timestamps are mixed up!");
                throw new RuntimeException("Timestamps are mixed up!");
            }
            var resultBar = new Bar(
                    baseCurrency,
                    baseCurrencyBars.get(i).getExchangeRate().divide(
                            quotedCurrencyBars.get(i).getExchangeRate(),
                            10,
                            RoundingMode.HALF_UP
                    ),
                    baseCurrencyBars.get(i).getTimestamp()
            );
            resultBars.add(resultBar);
        }

        return resultBars;
    }

    /**
     * @param instrument пара тикеров, разделенных дефисом
     * @param startTime unit timestamp начала интервала
     * @param endTime unit timestamp конца интервала
     * @return ежемесячные котировки по инструменту instrument в интервале между startTime и endTime,
     * в качестве котировки за месяц берется котировка за последний день этого месяца
     */
    public List<Bar> getMonthlyBarsInPeriod(String instrument, long startTime, long endTime) {

        var dailyBars = getDailyBarsInPeriod(instrument, startTime, endTime);

        return dailyBars.stream()
                .filter(
                        bar ->
                        LocalDateTime.ofEpochSecond(bar.getTimestamp(), 0, ZoneOffset.UTC)
                                .equals(LocalDateTime.ofEpochSecond(bar.getTimestamp(), 0, ZoneOffset.UTC)
                                        .with(TemporalAdjusters.lastDayOfMonth()))
                )
                .collect(Collectors.toList());
    }

    /**
     * @param instruments список инструментов, разделенных запятой,
     *                    instrument --- пара тикеров, разделенных дефисом
     * @param startTime unit timestamp начала интервала
     * @param endTime unit timestamp конца интервала
     * @return ежедневные котировки по инструментам в instruments в интервале между startTime и endTime
     */
    public Map<String, List<Bar>> getDailyBarsInPeriodForSeveralInstruments(
            String instruments,
            long startTime,
            long endTime
    ) {
        var instrumentList = instruments.split(",");
        var result = new HashMap<String, List<Bar>>();
        for (var instrument : instrumentList) {
            var bars = getDailyBarsInPeriod(instrument, startTime, endTime);
            result.put(instrument, bars);
        }
        return result;
    }

    /**
     * @param instruments список инструментов, разделенных запятой,
     *                    instrument --- пара тикеров, разделенных дефисом
     * @param startTime unit timestamp начала интервала
     * @param endTime unit timestamp конца интервала
     * @return ежемесячные котировки по инструментам в instruments в интервале между startTime и endTime
     */
    public Map<String, List<Bar>> getMonthlyBarsInPeriodForSeveralInstruments(
            String instruments,
            long startTime,
            long endTime
    ) {
        var instrumentList = instruments.split(",");
        var result = new HashMap<String, List<Bar>>();
        for (var instrument : instrumentList) {
            var bars = getMonthlyBarsInPeriod(instrument, startTime, endTime);
            result.put(instrument, bars);
        }
        return result;
    }

    /**
     * @param instrument строка, содержащая тикеры 2 активов, разделенных дефисом
     * @return пара активов
     */
    private Pair<Asset, Asset> getCurrencyPairFromInstrumentString(String instrument) {

        var currencyPair = Lists.newArrayList(Splitter.on("-").split(instrument));
        if (currencyPair.size() != 2) {
            throw new NotFoundException("Invalid instrument");
        }

        var baseCurrency = new Asset(currencyPair.get(0));
        var quotedCurrency = new Asset(currencyPair.get(1));

        if (assetService.findByCode(baseCurrency.getCode()).isEmpty()) {
            log.warn("Base currency {} from request is not supported", baseCurrency);
            throw new NotFoundException(
                    "Base currency" + baseCurrency + "from request is not supported"
            );
        }
        if (assetService.findByCode(quotedCurrency.getCode()).isEmpty()) {
            log.warn("Quoted currency {} from request is not supported", quotedCurrency);
            throw new NotFoundException(
                    "Quoted currency" + quotedCurrency + "from request is not supported"
            );
        }

        baseCurrency = assetService.findByCode(baseCurrency.getCode()).get();
        quotedCurrency = assetService.findByCode(quotedCurrency.getCode()).get();

        return new Pair<>(baseCurrency, quotedCurrency);
    }

    /**
     * @param instrument пара тикеров, разделенных дефисом
     * @return текущий курс по инструменту instrument, хранящийся в базе данных
     */
    public Ticker getTicker(String instrument) {

        final var currencyPair = getCurrencyPairFromInstrumentString(instrument);
        final var baseCurrency = currencyPair.getFirst();
        final var quotedCurrency = currencyPair.getSecond();

        var baseCurrencyTicker = tickerService.findByBaseAsset(baseCurrency);
        var quotedCurrencyTicker = tickerService.findByBaseAsset(quotedCurrency);

        return new Ticker(
                baseCurrency,
                baseCurrencyTicker.getExchangeRate().divide(
                        quotedCurrencyTicker.getExchangeRate(),
                        10,
                        RoundingMode.HALF_UP
                ),
                baseCurrencyTicker.getTimestamp()
        );
    }

    /**
     * @param instruments список инструментов, разделенных запятой,
     *                    instrument --- пара тикеров, разделенных дефисом
     * @return текущий курс по инструменту instrument, хранящийся в базе данных
     */
    public Map<String, Ticker> getTickers(String instruments) {

        var instrumentList = instruments.split(",");
        var result = new HashMap<String, Ticker>();
        for (var instrument : instrumentList) {
            var ticker = getTicker(instrument);
            result.put(instrument, ticker);
        }
        return result;
    }
}
