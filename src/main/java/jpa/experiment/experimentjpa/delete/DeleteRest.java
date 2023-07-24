package jpa.experiment.experimentjpa.delete;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jpa.experiment.experimentjpa.config.R4jBulkHeadConfig;
import jpa.experiment.experimentjpa.config.R4jCircuitBreakerConfig;
import jpa.experiment.experimentjpa.config.RestTemplateWrapper;
import jpa.experiment.experimentjpa.exception.ExceptionMessage;
import jpa.experiment.experimentjpa.model.ListenerMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
@Log4j2
public class DeleteRest {

    private ListenerMapper mapper;
    private HttpHeaders httpHeaders;
    private final RestTemplate restTemplate;
    private final CircuitBreaker altcraftServiceCB;
    private final Bulkhead altcraftServiceBH;
    private final RestTemplateWrapper restTemplateWrapper;
    private static final String ALTCRAFT_SERVICE_BREAKER = "altcraft-service-cb";
    private static final String ALTCRAFT_BH = "arca-bulkhead";

//    @Value("${alt-craft.test.url}")
    private String deleteUrl = "https://cdp.uzum.io/api/v1.1/profiles/delete";

    public DeleteRest(ListenerMapper mapper,
                        RestTemplate restTemplate, RestTemplateWrapper restTemplateWrapper,
                        CircuitBreakerRegistry circuitBreakerRegistry,
                        BulkheadRegistry bulkheadRegistry) {
        this.mapper = mapper;
        this.restTemplate = restTemplate;
        this.altcraftServiceCB = circuitBreakerRegistry.circuitBreaker(ALTCRAFT_SERVICE_BREAKER, R4jCircuitBreakerConfig.MERCHANT_SERVICE_CBC);
        this.altcraftServiceBH = bulkheadRegistry.bulkhead(ALTCRAFT_BH, R4jBulkHeadConfig.MERCHANT_SERVICE_BHC);
        this.restTemplateWrapper = restTemplateWrapper;
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set("Content-Type", "application/json");
    }

    public void sendForDeleteProfile(String phone) {

        try {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.setToken("e29bee5f98e8479bb4e8fe6831f39359");
            deleteRequest.setPhone(phone);
            sendRequest(deleteRequest);
        } catch (ExceptionMessage e) {
            throw new RuntimeException(e);
        }

    }
    public void  sendRequest(DeleteRequest deleteRequest) throws ExceptionMessage{
        try {
            restTemplateWrapper.doRequest(
                    restTemplate,
                    altcraftServiceCB,
                    altcraftServiceBH,
                    true,
                    deleteUrl,
                    HttpMethod.POST,
                    deleteRequest,
                    httpHeaders,
                    new ParameterizedTypeReference<DeleteResponse>() {
                    },
                    new DeleteResponse(),
                    false);


        } catch (HttpStatusCodeException e){
//            logger.error("error log while occurred on transferring users: ",
//                    ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
//                            .body(e.getResponseBodyAsString()));
        } catch (RestClientException ex){
        } catch (Exception exception){
        }


    }

}
