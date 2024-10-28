package com.upc.gessi.automation.rest.controllers;

import com.upc.gessi.automation.domain.controllers.IterationController;
import com.upc.gessi.automation.domain.controllers.SubjectController;
import com.upc.gessi.automation.rest.DTO.IterationDTO;
import com.upc.gessi.automation.rest.DTO.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/iteration")
public class IterationRestController {

    @Autowired
    IterationController iterationController;

    @Autowired
    SubjectController subjectController;

    @GetMapping
    public List<IterationDTO> getIterationsSubject(@RequestParam (name = "subject") String subject){
        return iterationController.getIterationSub(subject);
   }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createIteration(@RequestBody IterationDTO itDTO) throws ParseException {
        System.out.print("enter");
        String proj = subjectController.getProjects(itDTO.getSubject());
        IterationDTO i = new IterationDTO(itDTO.getName(),itDTO.getSubject(),itDTO.getFromData(),itDTO.getToData(),proj);
        iterationController.addIteration(i);
    }
    @PutMapping("/edit/{subject}")
    public void edit(@PathVariable String subject, @RequestBody List<String>projects){

        iterationController.addProject(subject,projects);
    }
}
