package jpa.experiment.experimentjpa.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ListenerMapper {

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

    public List<String> getIdentificationList(ListenerEntity user) {
        List<String> identification = new ArrayList<>();
        if(user.getCustomerId() != null) identification.add("Kapitalbank");
        if(user.getApelsinCustomerId() != null) identification.add("Apelsin");
        return identification;
    }
}
