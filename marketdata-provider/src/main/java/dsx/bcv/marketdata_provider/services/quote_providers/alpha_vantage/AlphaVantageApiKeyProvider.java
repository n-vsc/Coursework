package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AlphaVantageApiKeyProvider {

    private final List<String> apiKeyList = ImmutableList.of(
            "9AHDGW3TU1P53QD5",
            "KLSNN06MT5YKD8G6",
            "M17XFAE2JFPU6GB0",
            "M593Q9YBLNAHRXS9",
            "R8E6U7RI11YSJ9YU",
            "GPHASUAC6Y3JNZLH",
            "N9NMMTJL56RZO0EW",
            "CLWFP9W6HGD5KSO9",
            "X4T83HP3NZMB985U",
            "2Z98LLDBELVNIS9H",
            "3XFWJR1EBGNZ0LIG",
            "E4DD94ZAZ9Z1ZE0G",
            "SILL33XLLYXOXDEZ",
            "6YNTYM5J7YETR6X9",
            "FTBUJQ2P079OLHI9"
    );

    public String getApiKey() {
        return apiKeyList.get(new Random().nextInt(apiKeyList.size()));
    }
}
