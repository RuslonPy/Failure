package jpa.experiment.experimentjpa.service;

import jpa.experiment.experimentjpa.model.ListenerEntity;
import jpa.experiment.experimentjpa.model.ListenerEntityDto;
import jpa.experiment.experimentjpa.repository.JpaReposListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

    @Override
    public void deleteUser(Long id) {
        Optional<ListenerEntity> entity = repos.findById(id);
        if(entity.isPresent()){
            entity.get().setPhone(entity.get().getPhone());
            entity.get().setBirthday(entity.get().getBirthday());
            entity.get().setApelsinCustomerId(entity.get().getCustomerId());
            entity.get().setCustomerId(entity.get().getCustomerId());
            entity.get().setUserState(ListenerEntity.State.DELETED);
            repos.save(entity.get());
        }
    }
}
