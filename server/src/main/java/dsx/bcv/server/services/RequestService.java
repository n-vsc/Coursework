package dsx.bcv.server.services;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
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

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * Предоставляет возможность сделать POST запрос по указанному url
     * @param url url запроса
     * @param requestString тело запроса
     * @return текст, полученный в ответе на запрос
     */
    public String doPostRequest(String url, String requestString) {
        log.trace("doGetRequest called. Url: {}", url);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(requestString, JSON))
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

        try {
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

