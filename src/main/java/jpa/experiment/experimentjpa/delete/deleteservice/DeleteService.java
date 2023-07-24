package jpa.experiment.experimentjpa.delete.deleteservice;

import org.springframework.stereotype.Repository;

@Repository
public interface DeleteService {
    void sendForDeleting(String user);
}
