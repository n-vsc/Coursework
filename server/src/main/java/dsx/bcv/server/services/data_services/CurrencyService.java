package dsx.bcv.server.services.data_services;

import dsx.bcv.server.data.models.Currency;
import dsx.bcv.server.data.repositories.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Предоставляет возможность сохранить актив в базу данных
 */
@Service
@Slf4j
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Transactional
    public Currency save(Currency currency) {
        var currencyOptional = currencyRepository.findByCode(currency.getCode());
        return currencyOptional.orElseGet(() -> currencyRepository.save(currency));
    }
}
