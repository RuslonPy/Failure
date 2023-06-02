package jpa.experiment.experimentjpa.failure;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jpa.experiment.experimentjpa.altcraft.UserAltcraft;
import jpa.experiment.experimentjpa.altcraft.UserAltcraftRequest;
import jpa.experiment.experimentjpa.altcraft.UserAltcraftResponse;
import jpa.experiment.experimentjpa.exception.ExceptionMessage;
import jpa.experiment.experimentjpa.model.ListenerDto;
import jpa.experiment.experimentjpa.model.ListenerEntity;
import jpa.experiment.experimentjpa.model.ListenerMapper;
import jpa.experiment.experimentjpa.repository.JpaReposListener;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
public class FailedRequestService implements FailedService{
    private final FailedUserRepository failedUserRepository;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final ObjectMapper objectMapper;
    private final JpaReposListener jpaReposListener;
    private final UserAltcraft userAltcraft;
    private final ListenerMapper mapper;


//    @Value("alt-craft.test.url")
    private String testUrl = "e29bee5f98e8479bb4e8fe6831f39359";

    private final static String token = "e29bee5f98e8479bb4e8fe6831f39359";

    public FailedRequestService(@Lazy FailedUserRepository failedUserRepository, RestTemplate restTemplate, ObjectMapper objectMapper, @Lazy JpaReposListener jpaReposListener, @Lazy UserAltcraft userAltcraft, ListenerMapper mapper) {
        this.failedUserRepository = failedUserRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.jpaReposListener = jpaReposListener;
        this.userAltcraft = userAltcraft;
        this.mapper = mapper;
        this.httpHeaders = new HttpHeaders();
        this.httpHeaders.set("Content-Type", "application/json");
    }

    @Override
    public void saveFailedRequest(FailedRequestEntity entity) {
        FailedRequestEntity failedRequestEntity = new FailedRequestEntity();
        failedRequestEntity.setErrUserId(entity.getErrUserId());
        failedRequestEntity.setTimestamp(LocalDateTime.now());
        failedUserRepository.save(failedRequestEntity);
    }
//    @Scheduled(fixedDelay = 180000)   // (cron = "0 0 2 * * ?") xar kun kechasi soat 2 da.
//    public void sendFailedRequest(){
//        List<ListenerEntity> requestEntityList = jpaReposListener.findFailedUsers();
//
//        for (ListenerEntity failed : requestEntityList) {
//            try{
//                ListenerDto dto = mapper.mapUserAltcraftDto(failed);
//                UserAltcraftRequest altCraftRequest = new UserAltcraftRequest();
//                altCraftRequest.setDbId(1);
//                altCraftRequest.setToken(testUrl);
//                altCraftRequest.setData(dto);
//                sendRequest(altCraftRequest, dto.getId());
//                System.out.println("Scheduled method executed and send data");
//            } catch (ExceptionMessage e) {
//                throw new RuntimeException(e);
//            }
//        }
////                UserAltcraftRequest request = new ObjectMapper().readValue(failed.getDtoJson(), UserAltcraftRequest.class);
////                request.setToken(token);
//    }
//    public void  sendRequest(UserAltcraftRequest userAltcraftRequest, Long id) throws ExceptionMessage {
//
//        try {
//            HttpEntity<UserAltcraftRequest> httpEntity = new HttpEntity<>(userAltcraftRequest, httpHeaders);
//            UserAltcraftResponse body = restTemplate.postForEntity(testUrl, httpEntity, UserAltcraftResponse.class).getBody();
//            if (body.getError() == 0){
//                FailedRequestEntity failed = failedUserRepository.findByErrUserId(id);
//                failed.setStatus(RequestStatus.SENT);
//                failedUserRepository.save(failed);
//            }
//        } catch (HttpStatusCodeException e){
//
//            log.error("error log while occurred on transferring users: ",
//                    ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
//                            .body(e.getResponseBodyAsString()));
//        }
//    }

}

//    Map<String, Object> jsonMap = objectMapper.readValue(String.valueOf(failed), new TypeReference<Map<String, Object>>() {});
//    String token = (String) jsonMap.get("token");
//    Long userId = ((Number) jsonMap.get("data.user_ID")).longValue();
//    List<String> phones = (List<String>) jsonMap.get("data.phones");
//    Lang lang = Lang.valueOf((String) jsonMap.get("data.lang"));
//    String appVersion = (String) jsonMap.get("data.appVersion");
//    List<String> identification = (List<String>) jsonMap.get("data.identification");
//    Long regdate = ((Number) jsonMap.get("data._regdate")).longValue();
//    String city = (String) jsonMap.get("data._city");
//    String matching = (String) jsonMap.get("matching");
//    String fieldName = (String) jsonMap.get("field_name");
//    Boolean detectGeo = (Boolean) jsonMap.get("detect_geo");
//
//    ListenerDto dto = new ListenerDto();
//                dto.setId(userId);
//                        dto.setPhones(phones);
//                        dto.setLang(lang);
//                        dto.setAppVersion(appVersion);
//                        dto.setIdentification(identification);
//                        dto.setRegisteredDate(regdate);
//                        dto.setCity(city);
//
//                        UserAltcraftRequest request = new UserAltcraftRequest();
//                        request.setToken(token);
//                        request.setData(dto);
//                        request.setMatching(matching);
//                        request.setFieldName(fieldName);
//                        request.setDetectGeo(detectGeo);