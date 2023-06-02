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

@Component
@Log4j2
public class FailedRequestScheduler {

    private final FailedRequestService failedRequestService;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final JpaReposListener jpaReposListener;
    private final ListenerMapper mapper;

    private final FailedUserRepository failedUserRepository;

    @Value("${alt-craft.test.url}")
    private String testUrl;
    @Value("${alt-craft.test.token}")
    private String token;

    public FailedRequestScheduler(FailedRequestService failedRequestService, RestTemplate restTemplate, JpaReposListener jpaReposListener, ListenerMapper mapper, FailedUserRepository failedUserRepository) {
        this.failedRequestService = failedRequestService;
        this.restTemplate = restTemplate;
        this.jpaReposListener = jpaReposListener;
        this.mapper = mapper;
        this.failedUserRepository = failedUserRepository;
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set("Content-Type", "application/json");
    }

    @Scheduled(fixedDelay = 60000)   // (cron = "0 0 2 * * ?") xar kun kechasi soat 2 da.
    public void sendFailedRequest(){
        List<ListenerEntity> requestEntityList = jpaReposListener.findFailedUsers();

        for (ListenerEntity failed : requestEntityList) {
            try{
                ListenerDto dto = mapper.mapUserAltcraftDto(failed);
                UserAltcraftRequest altCraftRequest = new UserAltcraftRequest();
                altCraftRequest.setDbId(1);
                altCraftRequest.setToken(token);
                altCraftRequest.setData(dto);
                sendRequest(altCraftRequest, dto.getId());
                System.out.println("Scheduled method executed and send data");
            } catch (ExceptionMessage e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void  sendRequest(UserAltcraftRequest userAltcraftRequest, Long id) throws ExceptionMessage {

        try {
            HttpEntity<UserAltcraftRequest> httpEntity = new HttpEntity<>(userAltcraftRequest, httpHeaders);
            UserAltcraftResponse body = restTemplate.postForEntity(testUrl, httpEntity, UserAltcraftResponse.class).getBody();
            if (body.getError() == 0){
                FailedRequestEntity failed = failedUserRepository.findByErrUserId(id);
                failed.setStatus(RequestStatus.SENT);
                failedUserRepository.save(failed);
            }
        } catch (HttpStatusCodeException e){

            log.error("error log while occurred on transferring users: ",
                    ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                            .body(e.getResponseBodyAsString()));
        }
    }
}
