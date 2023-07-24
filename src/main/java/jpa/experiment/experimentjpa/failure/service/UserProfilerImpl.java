package jpa.experiment.experimentjpa.failure.service;

import jpa.experiment.experimentjpa.altcraft.UserAltcraft;
import jpa.experiment.experimentjpa.model.ListenerEntity;
import org.springframework.stereotype.Service;

@Service
public class UserProfilerImpl implements UserProfiler{
    private final UserAltcraft userAltcraft;

    public UserProfilerImpl(UserAltcraft userAltcraft) {
        this.userAltcraft = userAltcraft;
    }

    @Override
    public void sendProfile(ListenerEntity user) {
        userAltcraft.sendingProfile(user);
    }
}
