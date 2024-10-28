package com.upc.gessi.automation.rest.controllers;

import com.upc.gessi.automation.domain.controllers.SubjectController;
import com.upc.gessi.automation.domain.controllers.UserController;
import com.upc.gessi.automation.domain.models.Subject;
import com.upc.gessi.automation.rest.DTO.SubjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectRestController {

    @Autowired
    SubjectController subjectController;

    @Autowired
    UserController userController;
    
    @GetMapping(value="/token")
    public String getToken(@RequestParam(name = "name") String name){
        return subjectController.getTokenGit(name);
    }

    @GetMapping
    public List<SubjectDTO> getAll(){
        List<Subject> subjects = subjectController.getAll();
        List<SubjectDTO> result = new ArrayList<>();
        for(Subject s : subjects){
            SubjectDTO sub = new SubjectDTO(s.getName(),s.getGithub(),s.getTokenGithub(),s.getTaiga(),s.getSheets(),s.getUsername());
            result.add(sub);
        }
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createSubject(@RequestBody SubjectDTO subjectDTO){
        SubjectDTO sDTO = new SubjectDTO(subjectDTO.getName(),subjectDTO.getGithub(),subjectDTO.getToken_github(),subjectDTO.getTaiga(),subjectDTO.getSheets(),subjectDTO.getUsername());
        userController.createUser(subjectDTO.getUsername());
        subjectController.create(sDTO);
    }
}
