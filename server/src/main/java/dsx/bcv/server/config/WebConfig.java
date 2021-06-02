package dsx.bcv.server.config;

import dsx.bcv.server.converters.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Предназначен для инициализации конвертеров, которыми можно пользоваться через ConversionService
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new CurrencyToCurrencyVOConverter());
        registry.addConverter(new CurrencyVOToCurrencyConverter());
        registry.addConverter(new InstrumentToInstrumentVOConverter());
        registry.addConverter(new InstrumentVOToInstrumentConverter());
        registry.addConverter(new TradeToTradeVOConverter());
        registry.addConverter(new TradeVOToTradeConverter());
        registry.addConverter(new TransactionToTransactionVOConverter());
        registry.addConverter(new TransactionVOToTransactionConverter());
        registry.addConverter(new PortfolioToPortfolioVOConverter());
        registry.addConverter(new PortfolioVOToPortfolioConverter());
        registry.addConverter(new UserToUserVOConverter());
        registry.addConverter(new UserVOToUserConverter());
    }
}
