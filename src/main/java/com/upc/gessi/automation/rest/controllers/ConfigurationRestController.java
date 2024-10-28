package com.upc.gessi.automation.rest.controllers;

import com.upc.gessi.automation.domain.controllers.*;
import com.upc.gessi.automation.domain.models.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigurationRestController {

    @Autowired
    SubjectController subjectController;

    @Autowired
    MetricController metricController;

    @Autowired
    ProjectController projectController;

    @Autowired
    FactorController factorController;
    @Autowired
    StrategicIndicatorController strategicIndicatorController;

    @Autowired
    ConfigurationController configurationController;
    @Async
    @GetMapping(value = "/status")
    public Integer getStatus(@RequestParam(name = "id") Integer id){
        return configurationController.getActualStatus(id);
    }

    @PostMapping()
    //public Boolean getconfigProject(@RequestParam(name="name") String name, @RequestParam(name= "subject") String subject){
    public List<String> getconfigProject(@RequestBody List<Map<String,String>> parameters) throws IOException, InterruptedException {
        List<String> projects = new ArrayList<>();
        String name = null;
        for (Map<String, String> project : parameters) {
            name = project.get("name");
            String subject = project.get("subject");
            System.out.print(name);
            System.out.print(subject);

            configurationController.configure_connect(name, subject);
            configurationController.configure_qreval(name, subject);


            projects.add(name);
            /*if (subjectController.getGithub(subject)) {
                configurationController.configQR_connect_configGT(name, subject, "github");
                configurationController.configQR_connect_script(name, subject, "github");
                configurationController.configQR_connect_configM(name, subject, "github");
            }
            if (subjectController.getTaiga(subject)) {
                configurationController.configQR_connect_configGT(name, subject, "taiga");
                configurationController.configQR_connect_script(name, subject, "taiga");
                configurationController.configQR_connect_configM(name, subject, "taiga");
            }
            if (subjectController.getSheets(subject)) {
                configurationController.configQR_connect_configGT(name, subject, "sheets");
                configurationController.configQR_connect_script(name, subject, "sheets");
            }
            configurationController.configureEval(name,subject);*/


        }
        Integer id = configurationController.createConfiguration(projects.size());
        projectController.putconfig(name, id);
        return projects;

    }

    @PostMapping(value="/finish")
    public void finish(@RequestBody List<String> projects){
        for(String p : projects){
            projectController.finishConfig(p);
        }
    }

    //@GetMapping(value = "/")

    /*@GetMapping(value="/eval")
    public Boolean getEvalProject (@RequestParam Map<String,String[]> parameters) throws IOException, InterruptedException {
        for (Map.Entry<String, String[]> project : parameters.entrySet()) {
            String key = project.getKey();
            String values = String.valueOf(project.getValue());
            String[] params = values.split(",");
            String name = params[0];
            String subject = params[1];

            configurationController.getEvalProjects(name, subject);
            System.out.print("Entra EVALProject \n");
            configurationController.createFolderProject(name, subject);
            System.out.print("Passa crearFolder \n");
            configurationController.configQR_eval_script(name, subject);
        }
        return true;

    }*/


    @GetMapping(value = "/imports")
    public Boolean getImports(){
        return configurationController.importData();
    }

    /*@PostMapping(value = "/LD")
    public void configLD(@RequestParam(name = "projects") String project){
        System.out.print("ADD_METRICS");
        metricController.addMetrics(project);
        System.out.print("SET_FACTOR");
        metricController.setFactorMetric(project);
        System.out.print("ADD_CATEGORY_METRIC");
        metricController.addCategoryMetric(project);
        System.out.print("AAADAKJDKHDKDJHjhg");
    }*/




}
