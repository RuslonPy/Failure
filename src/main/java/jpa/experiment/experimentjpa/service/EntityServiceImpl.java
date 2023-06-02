package jpa.experiment.experimentjpa.service;

import jpa.experiment.experimentjpa.model.ListenerDto;
import jpa.experiment.experimentjpa.model.ListenerEntity;
import jpa.experiment.experimentjpa.model.ListenerEntityDto;
import jpa.experiment.experimentjpa.repository.JpaReposListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntityServiceImpl implements EntityService{

    private final JpaReposListener repos;
    @Override
    public void save(ListenerEntityDto dto) {
        ListenerEntity entity = new ListenerEntity();
        BeanUtils.copyProperties(dto, entity, "id");
        repos.save(entity);
    }

    @Override
    public void update(ListenerEntityDto dto, Long id) {
        Optional<ListenerEntity> entity = repos.findById(id);
        BeanUtils.copyProperties(dto, entity.get());
        entity.get().setId(id);
        repos.save(entity.get());
    }
}
