package jpa.experiment.experimentjpa.failure;

import jpa.experiment.experimentjpa.altcraft.UserAltcraft;
import jpa.experiment.experimentjpa.model.Lang;
import jpa.experiment.experimentjpa.model.ListenerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
@Component
@RequiredArgsConstructor
public class MyEntityListener {
    private final UserAltcraft userAltcraft;

    @PostPersist
    @PostUpdate
    public void listenToFields(ListenerEntity entity){

        String phone = entity.getPhone();

        String customerId = entity.getCustomerId();

        Long registeredDate = entity.getRegisteredDate();

        Timestamp birthday = entity.getBirthday();

        String appVersion = entity.getAppVersion();

        String apelsinCustomerId = entity.getApelsinCustomerId();

        String city = entity.getCity();

        Lang lang = entity.getLang();

        if (!StringUtils.isEmpty(phone) || !StringUtils.isEmpty(customerId) || Objects.nonNull(registeredDate) ||
                Objects.nonNull(birthday) || !StringUtils.isEmpty(appVersion)
             || !StringUtils.isEmpty(apelsinCustomerId) || !StringUtils.isEmpty(city) || Objects.nonNull(lang)){

            userAltcraft.sendingProfile(entity);
        }


    }

}
