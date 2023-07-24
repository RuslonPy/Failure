package jpa.experiment.experimentjpa.failure.service;


import jpa.experiment.experimentjpa.model.ListenerEntity;

public interface UserProfiler {
    void sendProfile(ListenerEntity user);
}
