package jpa.experiment.experimentjpa.delete.deleteservice;

import jpa.experiment.experimentjpa.delete.DeleteRest;
import org.springframework.stereotype.Service;

@Service
public class DeleteServiceImpl implements DeleteService{

    private final DeleteRest delete;

    public DeleteServiceImpl(DeleteRest delete) {
        this.delete = delete;
    }

    @Override
    public void sendForDeleting(String phone) {
        delete.sendForDeleteProfile(phone);
    }
}
