package jpa.experiment.experimentjpa.altcraft;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jpa.experiment.experimentjpa.config.R4jBulkHeadConfig;
import jpa.experiment.experimentjpa.config.R4jCircuitBreakerConfig;
import jpa.experiment.experimentjpa.config.RestTemplateWrapper;
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
    private  ListenerMapper mapper;
    private HttpHeaders httpHeaders;
    private FailedRequestService failedRequestService;
    private final RestTemplate restTemplate;
    private final CircuitBreaker altcraftServiceCB;
    private final Bulkhead altcraftServiceBH;
    private final RestTemplateWrapper restTemplateWrapper;
    private final FailedUserRepository failedUserRepository;

//    private final LogService logService;
//    @Value("${alt-craft.test.url}")
    private String testUrl = "https://cdp.uzum.io/api/v1.1/profiles/import";

    public UserAltcraft(ListenerMapper mapper,
                        FailedRequestService failedRequestService,
                        RestTemplate restTemplate, RestTemplateWrapper restTemplateWrapper,
                        CircuitBreakerRegistry circuitBreakerRegistry,
                        BulkheadRegistry bulkheadRegistry,
                        @Lazy FailedUserRepository failedUserRepository) {
        this.mapper = mapper;
        this.failedRequestService = failedRequestService;
        this.restTemplate = restTemplate;
        this.altcraftServiceCB = circuitBreakerRegistry.circuitBreaker("altcraft-service-cb", R4jCircuitBreakerConfig.MERCHANT_SERVICE_CBC);
        this.altcraftServiceBH = bulkheadRegistry.bulkhead("altcraft-bh", R4jBulkHeadConfig.MERCHANT_SERVICE_BHC);
        this.restTemplateWrapper = restTemplateWrapper;
        this.failedUserRepository = failedUserRepository;
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set("Content-Type", "application/json");
    }

    @Async
    public void sendingProfile(ListenerEntity user) {

        try {
            UserAltcraftRequest altCraftRequest = mapper.mapUserAltcraftRequest(user);
            sendRequest(altCraftRequest);
        } catch (ExceptionMessage e) {
            throw new RuntimeException(e);
        }

    }

    ObjectMapper objectMapper = new ObjectMapper();

    public void  sendRequest(UserAltcraftRequest userAltcraftRequest) throws ExceptionMessage{

        UserAltcraftResponse response = null;
        try {
            response = restTemplateWrapper.doRequest(
                    restTemplate,
                    altcraftServiceCB,
                    altcraftServiceBH,
                    true,
                    testUrl,
                    HttpMethod.POST,
                    userAltcraftRequest,
                    httpHeaders,
                    new ParameterizedTypeReference<UserAltcraftResponse>() {
                    },
                    new UserAltcraftResponse(),
                    false);

            if (isErrorResponse(response)){
                Long id = userAltcraftRequest.getData().getId();
                failedUserRepository.findByErrUserId(id).ifPresentOrElse(
                        entity -> {
                            entity.setTimestamp(LocalDateTime.now());
                            System.out.println("--------------------");
                            failedRequestService.saveFailedRequest(entity);
                        },
                        () -> {
                            FailedRequestEntity failedRequest = new FailedRequestEntity();
                            failedRequest.setErrUserId(id);
                            failedRequest.setStatus(RequestStatus.FAILED);
                            failedRequest.setTimestamp(LocalDateTime.now());
                            failedRequestService.saveFailedRequest(failedRequest);
                        }
                );
            }


        } catch (HttpStatusCodeException e){
            log.error("error log while occurred on transferring users: ",
                    ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                            .body(e.getResponseBodyAsString()));
        } catch (RestClientException ex){
            // log rest client exception.
        } catch (Exception exception){
//            Long id = userAltcraftRequest.getData().getId();
//            getFailedRequestById(id).ifPresent(entity -> {
//                entity.setTimestamp(LocalDateTime.now());
//            });
//            failed = new FailedRequestEntity();
//            failed.setErrUserId(id);
//            failed.setStatus(RequestStatus.FAILED);
////            FailedRequestEntity entity = new FailedRequestEntity();
////            entity.setErrUserId(userAltcraftRequest.getData().getId());
////            entity.setStatus(RequestStatus.FAILED);
//            failedRequestService.saveFailedRequest(failed);
        }


    }

    public boolean isErrorResponse(UserAltcraftResponse response) {
        return response == null || response.getError() != 0;
    }
//    private Optional<FailedRequestEntity> getFailedRequestById(Long id) {
//        return failedUserRepository.findByErrUserId(id);
//    }

    public UserAltcraftResponse response(){
        UserAltcraftResponse user = new UserAltcraftResponse();
        user.setError(user.getError());
        user.setError_text(user.getError_text());
        user.setProfile_id(user.getProfile_id());
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
