package com.upc.gessi.automation.domain.controllers;

import com.upc.gessi.automation.domain.models.Subject;
import com.upc.gessi.automation.domain.respositories.SubjectRepository;
import com.upc.gessi.automation.rest.DTO.SubjectDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SubjectController {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    ProjectController projectController;

    public List<Subject> getAll(){
        List<Subject> subjects_names = new ArrayList<>();
        Iterable<Subject> subjects =  subjectRepository.findAll();
        for(Subject sub: subjects){
            subjects_names.add(sub);
        }
        return subjects_names;
    }

    public void create(@NotNull SubjectDTO sub){
        Subject s = new Subject(sub.getName(),sub.getGithub(),sub.getToken_github(),sub.getTaiga(),sub.getSheets(),sub.getUsername());
        subjectRepository.save(s);
    }

    public String getTokenGit(String name){
        Subject sub = subjectRepository.findByName(name);
        return sub.getTokenGithub(); // Devuelve el ID del primer proyecto encontrado
    }
    public Boolean getTaiga(String name){
        Subject sub = subjectRepository.findByName(name);
        System.out.print(sub.getTaiga());
        return sub.getTaiga();
    }
    public Boolean getGithub(String name){
        Subject sub = subjectRepository.findByName(name);
        return sub.getGithub();
    }
    public Boolean getSheets(String name){
        Subject sub = subjectRepository.findByName(name);
        return sub.getSheets();
    }

    public String getProjects(String subject){
        List<Integer> proj = projectController.getProjectsSubject(subject);
        System.out.println(proj.toString());
        return proj.toString();
    }

}
