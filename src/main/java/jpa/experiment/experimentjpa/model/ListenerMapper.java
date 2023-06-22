package jpa.experiment.experimentjpa.model;

import jpa.experiment.experimentjpa.altcraft.UserAltcraftRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

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
        ListenerDto userAltcraftDto = mapUserAltcraftDto(user);
        UserAltcraftRequest userAltcraftRequest = new UserAltcraftRequest();
        userAltcraftRequest.setDbId(1);
        userAltcraftRequest.setToken(token);
        userAltcraftRequest.setData(userAltcraftDto);
        return userAltcraftRequest;
    }

    public List<String> getIdentificationList(ListenerEntity user) {
        List<String> identification = new ArrayList<>();
        if(user.getCustomerId() != null) identification.add("Kapitalbank");
        if(user.getApelsinCustomerId() != null) identification.add("Apelsin");
        return identification;
    }
}
