package com.upc.gessi.automation.rest.controllers;

import com.upc.gessi.automation.domain.controllers.FactorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/factors")
public class FactorRestController {

    @Autowired
    FactorController factorController;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createFactors(@RequestBody List<String> projects){
        //factorController.createFactorCategory("Reversed Default");
        for(String p : projects) {
            factorController.createFactors(p);
        }
    }

    @PostMapping(value = "/LD")
    public void postFactors(@RequestBody List<String> projects){
        for(String project : projects) {
            factorController.postFactor(project);
        }
    }

}
