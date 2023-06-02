package jpa.experiment.experimentjpa.service;

import jpa.experiment.experimentjpa.model.ListenerDto;
import jpa.experiment.experimentjpa.model.ListenerEntityDto;

public interface EntityService {
    void save(ListenerEntityDto dto);
    void update(ListenerEntityDto dto, Long id);
}
