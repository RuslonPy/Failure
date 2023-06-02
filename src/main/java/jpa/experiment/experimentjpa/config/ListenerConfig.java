package jpa.experiment.experimentjpa.config;

import jpa.experiment.experimentjpa.altcraft.UserAltcraft;
import jpa.experiment.experimentjpa.model.ListenerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Component
@RequiredArgsConstructor
public class ListenerConfig {
    private final UserAltcraft userAltcraft;

    @PostPersist
    @PostUpdate
    public void  afterPostUpdate(ListenerEntity listener){
        userAltcraft.sendingProfile(listener);
    }

}
