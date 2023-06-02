package jpa.experiment.experimentjpa.failure;

import jpa.experiment.experimentjpa.model.ListenerEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedUserRepository extends JpaRepository<FailedRequestEntity, Long> {

    FailedRequestEntity findByErrUserId(Long id);

}
