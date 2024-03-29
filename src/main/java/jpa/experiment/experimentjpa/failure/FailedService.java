package jpa.experiment.experimentjpa.failure;

import jpa.experiment.experimentjpa.altcraft.UserAltcraftRequest;
import jpa.experiment.experimentjpa.altcraft.UserAltcraftResponse;

public interface FailedService {
    void updateFailedRequestStatus(Long id);
    void saveOrUpdateFailedRequest(UserAltcraftRequest userAltcraftRequest);
}
