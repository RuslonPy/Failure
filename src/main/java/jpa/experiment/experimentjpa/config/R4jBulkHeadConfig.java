package jpa.experiment.experimentjpa.config;

import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class R4jBulkHeadConfig {
    public static final String MERCHANT_SERVICE_BHC = "merchant-service";

    @Bean
    public BulkheadConfig globalBulkheadConfig() {
        return BulkheadConfig.ofDefaults();
    }

    @Bean
    public BulkheadConfig merchantServiceBulkheadConfig() {
        return BulkheadConfig.custom()
                .maxConcurrentCalls(30)
                .maxWaitDuration(Duration.ofSeconds(5))
                .build();
    }
    @Bean
    public BulkheadRegistry bulkheadRegistry() {
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.of(globalBulkheadConfig());
        bulkheadRegistry.addConfiguration(MERCHANT_SERVICE_BHC, merchantServiceBulkheadConfig());
        return bulkheadRegistry;
    }

}
