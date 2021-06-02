package dsx.bcv.server.services.api_connectors;

import dsx.bcv.server.services.api_connectors.tinkoff.TinkoffApiConnector;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Предназначен для хранения коннекторов к торговым площадкам и предоставляет возможность получить их по названию
 */
@Service
public class ApiConnectorRepository {

    private final Map<ApiConnectorName, ApiConnector> apiConnectors;

    public ApiConnectorRepository(TinkoffApiConnector tinkoffApiConnector) {

        this.apiConnectors = Map.of(
                ApiConnectorName.Tinkoff, tinkoffApiConnector
        );
    }

    /**
     *
     * @param apiConnectorName имя торговой площадки
     * @return экземпляр класса, позволяющий загрузить данные о сделках и транзакциях пользователя
     */
    public ApiConnector getApiConnectorByName(ApiConnectorName apiConnectorName) {
        return apiConnectors.get(apiConnectorName);
    }
}
