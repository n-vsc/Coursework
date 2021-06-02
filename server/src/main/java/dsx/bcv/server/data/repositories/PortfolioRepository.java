package dsx.bcv.server.data.repositories;

import dsx.bcv.server.data.models.Portfolio;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PortfolioRepository extends CrudRepository<Portfolio, Long> {
    Optional<Portfolio> findByName(String name);
}
