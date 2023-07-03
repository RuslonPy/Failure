package jpa.experiment.experimentjpa.failure;

import jpa.experiment.experimentjpa.altcraft.UserAltcraftRequest;
import jpa.experiment.experimentjpa.altcraft.UserAltcraftResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Log4j2
public class FailedRequestService implements FailedService{
    //<editor-fold desc="Whenever if users transfer fails">
    private final FailedUserRepository failedUserRepository;
    //</editor-fold>

    public FailedRequestService(@Lazy FailedUserRepository failedUserRepository) {
        this.failedUserRepository = failedUserRepository;
    }

    //<editor-fold desc="Description">
    @Override
    public void updateFailedRequestStatus(Long id, UserAltcraftResponse body) {
        if (body != null && body.getError() == 0) {
            failedUserRepository.findByErrUserId(id).ifPresent(failed -> {
                failed.setStatus(RequestStatus.SENT);
                failedUserRepository.save(failed);
            });
        }
    }
    //</editor-fold>

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveOrUpdateFailedRequest(UserAltcraftRequest userAltcraftRequest) {
        Long id = userAltcraftRequest.getData().getId();
        failedUserRepository.findByErrUserId(id).ifPresentOrElse(
                entity -> {
                    entity.setStatus(entity.getStatus() == RequestStatus.SENT ? RequestStatus.FAILED : entity.getStatus());
                    entity.setFailOccuredTime(LocalDateTime.now());
                    failedUserRepository.save(entity);
                },
                () -> {
                    FailedRequestEntity failedRequest = new FailedRequestEntity();
                    failedRequest.setErrUserId(id);
                    failedRequest.setStatus(RequestStatus.FAILED);
                    failedRequest.setFailOccuredTime(LocalDateTime.now());
                    failedUserRepository.save(failedRequest);
                }
        );
    }

}
