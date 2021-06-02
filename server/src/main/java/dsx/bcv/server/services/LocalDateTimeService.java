package dsx.bcv.server.services;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Используется для преобразования даты из строки, предоставляемой Dsx Global,
 * в строку, которую способен распарсить LocalDateTime
 */
@Service
public class LocalDateTimeService {

    public LocalDateTime getDateTimeFromString(@NonNull String dateString) {
        var spacePos = dateString.indexOf(' ');
        if (dateString.charAt(spacePos + 2) == ':') {
            dateString = dateString.substring(0, spacePos + 1) + '0' + dateString.substring(spacePos + 1);
        }

        final String localDateTimeFormat = dateString.replace(' ', 'T');
        return LocalDateTime.parse(localDateTimeFormat);
    }
}
