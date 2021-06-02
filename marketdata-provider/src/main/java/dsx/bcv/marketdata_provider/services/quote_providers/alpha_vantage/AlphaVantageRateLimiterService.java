package dsx.bcv.marketdata_provider.services.quote_providers.alpha_vantage;

import com.google.common.util.concurrent.RateLimiter;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class AlphaVantageRateLimiterService {

    private final double callsPerMinute = 5 - 1;
    private final int secondsInMinute = 60;
    @Getter
    private final RateLimiter rateLimiter = RateLimiter.create(callsPerMinute / secondsInMinute);
}
