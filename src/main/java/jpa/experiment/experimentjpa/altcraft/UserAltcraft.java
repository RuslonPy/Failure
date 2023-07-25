package jpa.experiment.experimentjpa.altcraft;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jpa.experiment.experimentjpa.config.R4jBulkHeadConfig;
import jpa.experiment.experimentjpa.config.R4jCircuitBreakerConfig;
import jpa.experiment.experimentjpa.config.RestTemplateWrapper;
import jpa.experiment.experimentjpa.delete.DeleteRequest;
import jpa.experiment.experimentjpa.exception.ExceptionMessage;
import jpa.experiment.experimentjpa.failure.FailedRequestEntity;
import jpa.experiment.experimentjpa.failure.FailedRequestService;
import jpa.experiment.experimentjpa.failure.FailedUserRepository;
import jpa.experiment.experimentjpa.failure.RequestStatus;
import jpa.experiment.experimentjpa.model.ListenerEntity;
import jpa.experiment.experimentjpa.model.ListenerMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j2
public class UserAltcraft {
    private ListenerMapper mapper;
    private HttpHeaders httpHeaders;
    private FailedRequestService failedRequestService;
    private final RestTemplate restTemplate;
    private final CircuitBreaker altcraftServiceCB;
    private final Bulkhead altcraftServiceBH;
    private final RestTemplateWrapper restTemplateWrapper;
    private static final String ALTCRAFT_SERVICE_BREAKER = "altcraft-service-cb";
    private static final String ALTCRAFT_BH = "arca-bulkhead";

    //    private final LogService logService;
//    @Value("${alt-craft.test.url}")
    private String userUrl = "https://cdp.uzum.io/api/v1.1/profiles/import";
    private String deleteUrl = "https://cdp.uzum.io/api/v1.1/profiles/delete";

    public UserAltcraft(ListenerMapper mapper,
                        FailedRequestService failedRequestService,
                        RestTemplate restTemplate, RestTemplateWrapper restTemplateWrapper,
                        CircuitBreakerRegistry circuitBreakerRegistry,
                        BulkheadRegistry bulkheadRegistry) {
        this.mapper = mapper;
        this.failedRequestService = failedRequestService;
        this.restTemplate = restTemplate;
        this.altcraftServiceCB = circuitBreakerRegistry.circuitBreaker(ALTCRAFT_SERVICE_BREAKER, R4jCircuitBreakerConfig.MERCHANT_SERVICE_CBC);
        this.altcraftServiceBH = bulkheadRegistry.bulkhead(ALTCRAFT_BH, R4jBulkHeadConfig.MERCHANT_SERVICE_BHC);
        this.restTemplateWrapper = restTemplateWrapper;
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set("Content-Type", "application/json");
    }

    public <T> void sendingProfile(T user) {
        try {
            if (ListenerEntity.State.DELETED == ((ListenerEntity) user).getUserState()) {
                DeleteRequest deleteRequest = new DeleteRequest();
                deleteRequest.setToken("e29bee5f98e8479bb4e8fe6831f39359");
                deleteRequest.setPhone(((ListenerEntity) user).getPhone());
                sendRequest(deleteRequest, deleteUrl);
            } else {
                UserAltcraftRequest altCraftRequest = mapper.mapUserAltcraftRequest((ListenerEntity) user);
                sendRequest(altCraftRequest, userUrl);
            }
        } catch (ExceptionMessage e) {
            throw new RuntimeException(e);
        }

    }

    ObjectMapper objectMapper = new ObjectMapper();

    public <T> void sendRequest(T request, String url) throws ExceptionMessage {

        UserAltcraftResponse response = null;
        try {
            response = restTemplateWrapper.doRequest(
                    restTemplate,
                    altcraftServiceCB,
                    altcraftServiceBH,
                    true,
                    url,
                    HttpMethod.POST,
                    request,
                    httpHeaders,
                    new ParameterizedTypeReference<>() {
                    },
                    new UserAltcraftResponse(),
                    false);

            if(request.getClass() == UserAltcraftRequest.class) {
                if (isErrorResponse(response)) {
                    failedRequestService.saveOrUpdateFailedRequest((UserAltcraftRequest) request);
                } else {
                    failedRequestService.updateFailedRequestStatus(((UserAltcraftRequest) request).getData().getId());
                }
            }

        } catch (HttpStatusCodeException e) {
            log.error("error log while occurred on transferring users: ",
                    ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                            .body(e.getResponseBodyAsString()));
        } catch (RestClientException ex) {
            // log rest client exception.
        } catch (Exception exception) {

        }


    }

    public boolean isErrorResponse(UserAltcraftResponse response) {
        return response == null || response.getError() != 0;
    }

    public UserAltcraftResponse response() {
        UserAltcraftResponse user = new UserAltcraftResponse();
        user.setError(user.getError());
        user.setErrorText(user.getErrorText());
        user.setProfileId(user.getProfileId());
        return user;
    }
//    private HttpHeaders getHeaders() {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON.toString());
//        return httpHeaders;
//    }

//    public UserAltcraftDto toDto(User user)
//
//    {
//        UserAltcraftDto userAltcraftDto = new UserAltcraftDto();
//        userAltcraftDto.setId(user.getId());
//        List<String> phones = new ArrayList<>();
//        phones.add(user.getPhone());
//        userAltcraftDto.setPhones(phones);
//        userAltcraftDto.setLang(user.getLang());
//        userAltcraftDto.setAppVersion(user.getAppVersion());
//        userAltcraftDto.setRegisteredDate(user.getRegisteredDate());
//        userAltcraftDto.setCity(user.getCity());
//        userAltcraftDto.setBirthday(user.getBirthday());
//        List<String> identification = new ArrayList<>();
//        if (user.getCustomerId() != null) identification.add("kapital");
//        if (user.getApelsinCustomerId() != null) identification.add("apelsin");
//        if (!identification.isEmpty()) userAltcraftDto.setIdentification(identification);
//        return userAltcraftDto;
//    }

}
