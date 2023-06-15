package jpa.experiment.experimentjpa.repository;

import jpa.experiment.experimentjpa.model.ListenerEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaReposListener extends JpaRepository<ListenerEntity, Long> {
    Optional<ListenerEntity> findById(Long id);

//    @Query(value = "SELECT l FROM listener_entity l WHERE l.id IN (SELECT f.err_user_id FROM failed_users f WHERE f.status = 'FAILED')", nativeQuery = true)
//    List<ListenerEntity> findFailedUsers();

//    @Query("SELECT l FROM ListenerEntity l WHERE l.id IN (SELECT f.errUserId FROM FailedRequestEntity f WHERE f.status = 'FAILED')")
//    List<ListenerEntity> findFailedUsers();

    @Query("SELECT u FROM ListenerEntity u JOIN FailedRequestEntity f ON u.id = f.errUserId WHERE f.status = 'FAILED'")
    List<ListenerEntity> findFailedUsers();

}
