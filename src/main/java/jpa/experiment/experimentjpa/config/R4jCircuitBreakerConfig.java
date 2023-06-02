package jpa.experiment.experimentjpa.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class R4jCircuitBreakerConfig {

    public static final String MERCHANT_SERVICE_CBC = "merchant-service";

    public CircuitBreakerConfig globalCircuitBreakerConfig(){
        return CircuitBreakerConfig.ofDefaults();
    }

    public CircuitBreakerConfig merchantServiceCBC() {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMinutes(1))
                .slowCallDurationThreshold(Duration.ofSeconds(59))
                .permittedNumberOfCallsInHalfOpenState(5)
                .minimumNumberOfCalls(10)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10).build();
    }
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(globalCircuitBreakerConfig());
        circuitBreakerRegistry.addConfiguration(MERCHANT_SERVICE_CBC, merchantServiceCBC());
        return circuitBreakerRegistry;
    }

}
