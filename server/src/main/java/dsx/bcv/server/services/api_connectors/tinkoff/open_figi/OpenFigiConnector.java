package dsx.bcv.server.services.api_connectors.tinkoff.open_figi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import dsx.bcv.server.exceptions.NotFoundException;
import dsx.bcv.server.services.RequestService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Коннектор к сервису Open Figi, на данный момент не используется
 */
@Service
public class OpenFigiConnector {

    private final RequestService requestService;
    private final String openfigiUrl = "https://api.openfigi.com/v2/mapping";

    public OpenFigiConnector(RequestService requestService) {
        this.requestService = requestService;
    }

    @SneakyThrows
    public List<String> convertFigisToTickers(List<String> figis) {

        var allRequestsList = figis.stream()
                .map(figi -> new OpenFigiRequestFormat("ID_BB_GLOBAL", figi))
                .collect(Collectors.toList());

        // max 10 elements per request
        var listOfRequestLists = Lists.partition(allRequestsList, 10);

        var resultList = new ArrayList<String>();
        for (var requestList : listOfRequestLists) {

            ObjectMapper objectMapper = new ObjectMapper();
            var jsonString = objectMapper.writeValueAsString(requestList);

            System.out.println(jsonString);

            String response = requestService.doPostRequest(openfigiUrl, jsonString);
            List<ResponseObject> responseObjectList = objectMapper.readValue(
                    response,
                    new TypeReference<List<ResponseObject>>() {}
            );

            resultList.addAll(responseObjectList.stream()
                    .map(responseObject -> responseObject.data.stream()
                            .findFirst()
                            .orElseThrow(NotFoundException::new)
                            .getTicker()
                            .toLowerCase()
                    )
                    .collect(Collectors.toList()));
        }

        return resultList;
    }
}
