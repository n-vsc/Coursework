package dsx.bcv.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ToJsonConverterService {

    private final ObjectMapper mapper = new ObjectMapper();

    public String toJson(final Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
}
