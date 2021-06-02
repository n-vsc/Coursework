package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage;

import dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage.models.AlphaVantageAsset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AlphaVantageCsvParser {

    public List<AlphaVantageAsset> parseAssets(Reader inputReader, char separator) {

        Iterable<CSVRecord> records;
        try {
            records = CSVFormat.newFormat(separator).parse(inputReader);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        List<AlphaVantageAsset> assets = new ArrayList<>();
        for (CSVRecord record : records) {
            var asset = new AlphaVantageAsset(
                    record.get(0),
                    record.get(1)
            );
            assets.add(asset);
        }

        return assets;
    }
}
