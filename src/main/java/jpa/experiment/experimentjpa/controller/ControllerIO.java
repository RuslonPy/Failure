package jpa.experiment.experimentjpa.controller;

import jpa.experiment.experimentjpa.altcraft.UserAltcraft;
import jpa.experiment.experimentjpa.altcraft.UserAltcraftResponse;
import jpa.experiment.experimentjpa.model.ListenerDto;
import jpa.experiment.experimentjpa.model.ListenerEntityDto;
import jpa.experiment.experimentjpa.service.EntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ControllerIO {

    private final EntityServiceImpl service;
    private final UserAltcraft userAltcraft;
    @PostMapping("/add")
    public UserAltcraftResponse save(@RequestBody ListenerEntityDto listenerDto){
        service.save(listenerDto);
        return userAltcraft.response();
    }

    @PutMapping("/update/{id}")
    public UserAltcraftResponse update(@RequestBody ListenerEntityDto listenerDto, @PathVariable("id") Long id){
        service.update(listenerDto, id);
        return new UserAltcraftResponse();
    }
}
