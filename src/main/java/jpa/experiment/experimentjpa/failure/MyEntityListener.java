package jpa.experiment.experimentjpa.failure;

import io.github.resilience4j.core.StringUtils;
import jpa.experiment.experimentjpa.altcraft.UserAltcraft;
import jpa.experiment.experimentjpa.delete.deleteservice.DeleteService;
import jpa.experiment.experimentjpa.failure.service.UserProfiler;
import jpa.experiment.experimentjpa.model.ListenerEntity;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.*;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class MyEntityListener {
    private final UserProfiler userProfiler;

    public MyEntityListener(@Lazy UserProfiler userProfiler) {
        this.userProfiler = userProfiler;
    }

    @PostPersist
    @PostUpdate
    public void listenToFields(ListenerEntity entity){

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter(){
            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED && isNotIt(entity)) {
                        CompletableFuture.runAsync(() -> userProfiler.sendProfile(entity));
                }
            }
        });


    }

    private boolean isNotIt(ListenerEntity entity){
        return StringUtils.isNotEmpty(entity.getPhone()) ||
                StringUtils.isNotEmpty(entity.getCustomerId()) ||
                Objects.nonNull(entity.getRegisteredDate()) ||
                Objects.nonNull(entity.getBirthday()) ||
                StringUtils.isNotEmpty(entity.getAppVersion()) ||
                StringUtils.isNotEmpty(entity.getApelsinCustomerId()) ||
                StringUtils.isNotEmpty(entity.getCity()) ||
                Objects.nonNull(entity.getLang());
    }



}
