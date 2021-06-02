package dsx.bcv.marketdata_provider.services.quote_providers.dsx;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dsx.bcv.marketdata_provider.services.RequestService;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.currency_graph.DsxInstrumentEdge;
import dsx.bcv.marketdata_provider.services.quote_providers.dsx.models.DsxInstrument;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DsxSupportedInstruments {

    @Getter
    private final Set<DsxInstrumentEdge> instruments;

    @Getter
    private final Set<DsxInstrumentEdge> reversedInstruments;

    private final RequestService requestService;
    private final ObjectMapper objectMapper;

    public DsxSupportedInstruments(RequestService requestService, ObjectMapper objectMapper) {

        this.requestService = requestService;
        this.objectMapper = objectMapper;

        instruments = initSupportedInstruments();
        log.info("Dsx supported instruments: {}", instruments);

        reversedInstruments = new HashSet<>();
        instruments.forEach(dsxInstrumentEdge -> reversedInstruments.add(dsxInstrumentEdge.reverse()));
    }

    @SneakyThrows
    private Set<DsxInstrumentEdge> initSupportedInstruments() {

        var dsxUrlInfo = "https://dsxglobal.com/mapi/v2/info";
        var responseBody = "[]";//requestService.doGetRequest(dsxUrlInfo);

        List<DsxInstrument> instruments = objectMapper.readValue(responseBody, new TypeReference<List<DsxInstrument>>() {});

        return instruments.stream()
                .map(dsxInstrument -> new DsxInstrumentEdge(
                        dsxInstrument.getBaseCurrency(),
                        dsxInstrument.getQuotedCurrency(),
                        false)
                )
                .collect(Collectors.toSet());
    }
}
