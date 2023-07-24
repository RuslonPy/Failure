package jpa.experiment.experimentjpa.failure;

import com.fasterxml.jackson.databind.ObjectMapper;
import jpa.experiment.experimentjpa.altcraft.UserAltcraftRequest;
import jpa.experiment.experimentjpa.altcraft.UserAltcraftResponse;
import jpa.experiment.experimentjpa.exception.ExceptionMessage;
import jpa.experiment.experimentjpa.model.ListenerDto;
import jpa.experiment.experimentjpa.model.ListenerEntity;
import jpa.experiment.experimentjpa.model.ListenerMapper;
import jpa.experiment.experimentjpa.repository.JpaReposListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Component
@Log4j2
public class FailedRequestScheduler {

    private final FailedRequestService failedRequestService;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final JpaReposListener jpaReposListener;
    private final ListenerMapper mapper;
    @Value("${alt-craft.test.url}")
    private String testUrl;

    public FailedRequestScheduler(FailedRequestService failedRequestService, RestTemplate restTemplate, JpaReposListener jpaReposListener, ListenerMapper mapper) {
        this.failedRequestService = failedRequestService;
        this.restTemplate = restTemplate;
        this.jpaReposListener = jpaReposListener;
        this.mapper = mapper;
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set("Content-Type", "application/json");
    }

    @Scheduled(fixedDelay = 300000)   // (cron = "0 0 2 * * ?") xar kun kechasi soat 2 da.
    public void sendFailedRequest(){
        List<ListenerEntity> requestEntityList = jpaReposListener.findFailedUsers();

        for (ListenerEntity failed : requestEntityList) {
            try{
                UserAltcraftRequest altCraftRequest = mapper.mapUserAltcraftRequest(failed);
                sendRequest(altCraftRequest, altCraftRequest.getData().getId());
                System.out.println("Scheduled method executed and send data");
            } catch (ExceptionMessage e) {
                throw new RuntimeException(e);
            }
        }
    }
    ObjectMapper objectMapper = new ObjectMapper();
    public void  sendRequest(UserAltcraftRequest userAltcraftRequest, Long id) throws ExceptionMessage {
        UserAltcraftResponse body = null;
        try {
            HttpEntity<UserAltcraftRequest> httpEntity = new HttpEntity<>(userAltcraftRequest, httpHeaders);
            body = restTemplate.postForEntity(testUrl, httpEntity, UserAltcraftResponse.class).getBody();
        } catch (HttpStatusCodeException e){

            log.error("error log while occurred on transferring users: ",
                    ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                            .body(e.getResponseBodyAsString()));
        }finally {
            failedRequestService.updateFailedRequestStatus(id, body);
//            if (body != null && body.getError() == 0){
//                failedUserRepository.findByErrUserId(id).ifPresent(failed -> {
//                    failed.setStatus(RequestStatus.SENT);
//                    failedRequestService.saveFailedRequest(failed);
//                });
//            }
        }
    }
}
