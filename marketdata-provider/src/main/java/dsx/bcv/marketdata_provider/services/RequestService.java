package dsx.bcv.marketdata_provider.services;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Предоставляет возможность делать HTTP запросы
 */
@Service
@Slf4j
public class RequestService {

    /**
     * Предоставляет возможность сделать GET запрос по указанному url
     * @param url url запроса
     * @return текст, полученный в ответе на запрос
     */
    public String doGetRequest(String url) {
        log.trace("doGetRequest called. Url: {}", url);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        log.trace("Response isSuccessful: {}", response.isSuccessful());
        log.trace("Response message: {}", response.message());
        log.trace("Response code: {}", response.code());
        if (!response.isSuccessful()) {
            log.trace("Retry request");
            return doGetRequest(url);
        }
        try {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
