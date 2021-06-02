package dsx.bcv.server.data.repositories;

import dsx.bcv.server.data.models.Trade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends CrudRepository<Trade, Long> {
}
