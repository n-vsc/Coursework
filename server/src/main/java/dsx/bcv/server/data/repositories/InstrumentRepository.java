package dsx.bcv.server.data.repositories;

import dsx.bcv.server.data.models.Currency;
import dsx.bcv.server.data.models.Instrument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstrumentRepository extends CrudRepository<Instrument, Long> {
    Optional<Instrument> findByBaseCurrencyAndQuotedCurrency(Currency firstCurrency, Currency secondCurrency);
}
