package dsx.bcv.marketdata_provider.controllers;

import dsx.bcv.marketdata_provider.services.QuoteProviderService;
import dsx.bcv.marketdata_provider.views.AssetVO;
import dsx.bcv.marketdata_provider.views.BarVO;
import dsx.bcv.marketdata_provider.views.TickerVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Предоставляет api для загрузки данных о котировках активов
 */
@RestController
@RequestMapping("quotes")
@Slf4j
public class QuoteProviderController {

    private final QuoteProviderService quoteProviderService;
    private final ConversionService conversionService;
    private final String instrumentExample = "Instruments example: eur-rub,yndx-usd.\n";

    public QuoteProviderController(QuoteProviderService quoteProviderService, ConversionService conversionService) {
        this.quoteProviderService = quoteProviderService;
        this.conversionService = conversionService;
    }

    @ApiOperation("Get supported assets")
    @GetMapping("assets")
    public List<AssetVO> getAssets() {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        return quoteProviderService.getAssets().stream()
                .map(currency -> conversionService.convert(currency, AssetVO.class))
                .collect(Collectors.toList());
    }

    @ApiOperation("Get bars for every day from startTime to endTime.\n" +
            "StartTime & endTime are Unix Timestamps in seconds (https://www.epochconverter.com).\n" +
            instrumentExample)
    @GetMapping("dailyBars/{instruments}/{startTime}/{endTime}")
    public Map<String, List<BarVO>> getDailyBarsInPeriod(
            @PathVariable String instruments,
            @PathVariable long startTime,
            @PathVariable long endTime
    ) {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        var barsMap = quoteProviderService.getDailyBarsInPeriodForSeveralInstruments(instruments, startTime, endTime);
        var convertedBarsMap = new HashMap<String, List<BarVO>>();
        for (var key : barsMap.keySet()) {
            convertedBarsMap.put(key, barsMap.get(key).stream()
                            .map(bar -> conversionService.convert(bar, BarVO.class))
                            .collect(Collectors.toList()));
        }
        return convertedBarsMap;
    }

    @ApiOperation("Get bars for every month from startTime to endTime.\n" +
            "StartTime & endTime are Unix Timestamps in seconds (https://www.epochconverter.com).\n" +
            instrumentExample)
    @GetMapping("monthlyBars/{instruments}/{startTime}/{endTime}")
    public Map<String, List<BarVO>> getMonthlyBarsInPeriod(
            @PathVariable String instruments,
            @PathVariable long startTime,
            @PathVariable long endTime
    ) {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        var barsMap = quoteProviderService.getMonthlyBarsInPeriodForSeveralInstruments(instruments, startTime, endTime);
        var convertedBarsMap = new HashMap<String, List<BarVO>>();
        for (var key : barsMap.keySet()) {
            convertedBarsMap.put(key, barsMap.get(key).stream()
                    .map(bar -> conversionService.convert(bar, BarVO.class))
                    .collect(Collectors.toList()));
        }
        return convertedBarsMap;
    }

    @ApiOperation("Get ticker.\n" + instrumentExample)
    @GetMapping("ticker/{instruments}")
    public Map<String, TickerVO> getTicker(@PathVariable String instruments) {
        log.info(
                "Request received. Url: {}",
                ServletUriComponentsBuilder.fromCurrentRequest().toUriString()
        );
        var tickersMap = quoteProviderService.getTickers(instruments);
        var convertedTickersMap = new HashMap<String, TickerVO>();
        for (var key : tickersMap.keySet()) {
            convertedTickersMap.put(
                    key,
                    conversionService.convert(tickersMap.get(key), TickerVO.class)
            );
        }
        return convertedTickersMap;
    }
}
