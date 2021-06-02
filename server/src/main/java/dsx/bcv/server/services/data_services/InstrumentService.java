package dsx.bcv.server.services.data_services;

import dsx.bcv.server.data.models.Instrument;
import dsx.bcv.server.data.repositories.InstrumentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Предоставляет возможность сохранить инструмент в базу данных
 */
@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final CurrencyService currencyService;

    public InstrumentService(InstrumentRepository instrumentRepository, CurrencyService currencyService) {
        this.instrumentRepository = instrumentRepository;
        this.currencyService = currencyService;
    }

    @Transactional
    public Instrument save(Instrument instrument) {
        var baseCurrency = currencyService.save(instrument.getBaseCurrency());
        var quotedCurrency = currencyService.save(instrument.getQuotedCurrency());
        var instrumentOptional = instrumentRepository.findByBaseCurrencyAndQuotedCurrency(
                baseCurrency,
                quotedCurrency
        );
        return instrumentOptional.orElseGet(
                () -> instrumentRepository.save(new Instrument(baseCurrency, quotedCurrency))
        );
    }
}
