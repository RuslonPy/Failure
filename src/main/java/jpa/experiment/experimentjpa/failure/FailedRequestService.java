package jpa.experiment.experimentjpa.failure;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Log4j2
public class FailedRequestService implements FailedService{
    private final FailedUserRepository failedUserRepository;
    public FailedRequestService(@Lazy FailedUserRepository failedUserRepository) {
        this.failedUserRepository = failedUserRepository;
    }

    @Override
    @Transactional
    public void saveFailedRequest(FailedRequestEntity entity) {
        entity.setErrUserId(entity.getErrUserId());
        entity.setStatus(entity.getStatus());
        entity.setTimestamp(entity.getTimestamp());
        failedUserRepository.save(entity);
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