package com.upc.gessi.automation.rest.controllers;

import com.upc.gessi.automation.domain.controllers.ProjectController;
import com.upc.gessi.automation.domain.controllers.SubjectController;
import com.upc.gessi.automation.domain.models.Subject;
import com.upc.gessi.automation.rest.DTO.ProjectDTO;

import com.upc.gessi.automation.rest.DTO.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/projects")
public class ProjectRestController {


    @Autowired
    ProjectController projectController;



    @Autowired

    private static final String NAME = "name";
    private static final String SUBJECT = "subject";
    private static final String URL_GITHUB = "URL_github";
    private static final String URL_TAIGA = "URL_taiga";
    private static final String URL_SHEETS = "URL_sheets";

    @GetMapping
    public List<ProjectDTO> getAll(){
        return projectController.getProjects();
    }

    @GetMapping(value="/id")
    public ProjectDTO getProjectId(@RequestParam(name = "name") String name ,@RequestParam(name = "subject") String subject){
        return projectController.getProject(name,subject);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProject(@RequestBody List<ProjectDTO> projectRequests){
        System.out.print("ENTRAAA");

        try{
            for(ProjectDTO projectRequest: projectRequests) {
                String name = projectRequest.getName();
                String subject = projectRequest.getSubject();
                String urlGithub = projectRequest.getUrlGithub();

                String urlTaiga = projectRequest.getUrlTaiga();
                String urlSheets = projectRequest.getUrlSheets();

                ProjectDTO pDTO = new ProjectDTO(name, subject, urlGithub, urlTaiga, urlSheets);
                projectController.createProject(pDTO);
            }
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping(value = "/{name}")
    public void updateProject(@PathVariable String name, @RequestBody ProjectDTO projectDTO){
        System.out.print(projectDTO);
        projectController.updatePrj(projectDTO);
    }

}
