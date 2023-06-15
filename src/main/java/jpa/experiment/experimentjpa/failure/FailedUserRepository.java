package jpa.experiment.experimentjpa.failure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FailedUserRepository extends JpaRepository<FailedRequestEntity, Long> {

    Optional<FailedRequestEntity> findByErrUserId(Long id);

}
