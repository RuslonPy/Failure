package jpa.experiment.experimentjpa.model;

import jpa.experiment.experimentjpa.altcraft.UserAltcraftRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@Mapper(componentModel = "spring")
public abstract class ListenerMapper {

    @Value("${alt-craft.test.token}")
    private String token;

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "phones", ignore = true)
    @Mapping(target = "lang", source = "user.lang")
    @Mapping(target = "appVersion", source = "user.appVersion")
    @Mapping(target = "identification", ignore = true)
    @Mapping(target = "registeredDate", source = "user.registeredDate")
    @Mapping(target = "birthday", source = "user.birthday")
    public abstract ListenerDto asUserAltcraftDto(ListenerEntity user);

    public ListenerDto mapUserAltcraftDto(ListenerEntity user){
        ListenerDto listenerDto = asUserAltcraftDto(user);
        listenerDto.setPhones(List.of(user.getPhone()));
        listenerDto.setIdentification(getIdentificationList(user));
        return listenerDto;
    }

    public UserAltcraftRequest mapUserAltcraftRequest(ListenerEntity user) {

//        if (user.getUserState() == ListenerEntity.State.DELETED) {
//            UserAltcraftRequest userAltcraftRequest = new UserAltcraftRequest();
//            userAltcraftRequest.setDbId(1);
//            userAltcraftRequest.setToken(token);
//            userAltcraftRequest.setMatching("phone");
//            userAltcraftRequest.setPhone(user.getPhone());
//            return userAltcraftRequest;
//        }
        ListenerDto userAltcraftDto = mapUserAltcraftDto(user);
        UserAltcraftRequest userAltcraftRequest = new UserAltcraftRequest();
        userAltcraftRequest.setToken(token);
        userAltcraftRequest.setDbId(1);
        userAltcraftRequest.setData(userAltcraftDto);
        return userAltcraftRequest;
    }

    public Map<String, String> getIdentificationList(ListenerEntity user) {
        Map<String, String> identification = new HashMap<>();
        identification.put("Kapital", user.getCustomerId() != null ? "true" : "false");
        identification.put("Apelsin", user.getApelsinCustomerId() != null ? "true" : "false");
        return identification;
    }
}
