package com.upc.gessi.automation.domain.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.upc.gessi.automation.domain.models.Project;
import com.upc.gessi.automation.domain.models.Student;
import com.upc.gessi.automation.domain.respositories.ProjectRepository;
import com.upc.gessi.automation.rest.DTO.ProjectDTO;
import com.upc.gessi.automation.rest.DTO.StudentDTO;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;


import java.net.URL;
import java.net.http.HttpClient;
import java.util.*;


@Controller
public class ProjectController {

    @Autowired
    ProjectRepository projectRep;

    public ProjectController(ProjectRepository projectRep) {
        this.projectRep = projectRep;
    }

    public ProjectDTO getProject(String name, String subject){
        Project project = projectRep.findByNameAndSubject(name,subject);
        ProjectDTO pDTO = new ProjectDTO(project.getId(), project.getName(),project.getSubject(),project.getURL_github(), project.getURL_taiga(), project.getURL_sheets(),project.getConfig());
        return pDTO;
    };

    public List<ProjectDTO>getProjects(){
        List<ProjectDTO> projects = new ArrayList<>();
        Iterable<Project> projectsit = projectRep.findAll();
        for(Project project : projectsit){
            System.out.print("IDDDD "+project.getConfig());
            ProjectDTO pDTO = new ProjectDTO(project.getId(), project.getName(),project.getSubject(),project.getURL_github(), project.getURL_taiga(), project.getURL_sheets(),project.getConfig());
            projects.add(pDTO);
        }
        return projects;
    }

    public void finishConfig(String name){
        Project p = projectRep.findByName(name);
        p.setConfig(true);
        projectRep.save(p);
    }

    public void createProject(ProjectDTO pDTO){
        if(projectRep.existsByNameAndSubject(pDTO.getName(),pDTO.getSubject())){
            System.out.print("Already exists"+pDTO.getName()+"  "+pDTO.getSubject());
        }
        else {
            Project project = new Project(pDTO.getName(), pDTO.getSubject(), pDTO.getUrlGithub(), pDTO.getUrlTaiga(), pDTO.getUrlSheets());
            projectRep.save(project);
        }
    }


    public Integer getId(String name, String subject) {
        Project project = projectRep.findByNameAndSubject(name, subject);
        return project.getId();
    }
    public String getIdGithub(String name, String subject){
        Project project = projectRep.findByNameAndSubject(name, subject);
        System.out.print(project.getID_github());
        return project.getID_github();
    }

    public String getIdTaiga(String name, String subject){
        Project project = projectRep.findByNameAndSubject(name, subject);
        return project.getID_taiga();
    }
    public String getIdSheets(String name, String subject){
        Project project = projectRep.findByNameAndSubject(name, subject);
        return project.getID_Sheets();
    }

    public void setConfig(String name){
        Project p = projectRep.findByName(name);
        p.setConfig_id(100);
        projectRep.save(p);
    }

    public void setNumStudents(Integer num,String name, String subject){
        Project p = projectRep.findByNameAndSubject(name,subject);
        p.setNum_students(num);
        projectRep.save(p);
    }

    public Integer getNumStudents(String name){
        Project p = projectRep.findByName(name);
        return p.getNum_students();
    }
    public Boolean isConfig(String name, String subject){
        Project p = projectRep.findByNameAndSubject(name,subject);
        if(p.getConfig_id() == 5){
            return true;
        }
        else return false;
    }

    public String getName(Integer id) {
        if(projectRep.existsById(id)){
            Project p = projectRep.findById(id).orElse(null);
            return p.getName();
        }
        return null;
    }

    public void updatePrj(ProjectDTO pDTO){
        Project p =  projectRep.findByNameAndSubject(pDTO.getName(), pDTO.getSubject());
        p.setURL_github(pDTO.getUrlGithub());
        p.setURL_taiga(pDTO.getUrlTaiga());
        p.setURL_sheets(pDTO.getUrlSheets());
        projectRep.save(p);
    }

    public void putconfig(String name, Integer id){
        Project p = projectRep.findByName(name);
        p.setConfig_id(id);
        projectRep.save(p);
    }

    public Integer getExternalId(String project){
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        try{
            Request getProjects = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/projects"))
                    .build();

            Response getResponse = client.newCall(getProjects).execute();
            if (getResponse.isSuccessful() ) {
                System.out.print("AAAAAAAAAA");
                ResponseBody data = getResponse.body();
                System.out.print(data);
                if(data !=null) {
                    String dataString = data.string();
                    System.out.println(dataString);
                    JsonArray json = JsonParser.parseString(dataString).getAsJsonArray();
                    for(int i= 0;i<json.size(); i++){
                        JsonObject element = json.get(i).getAsJsonObject();
                        if(element.get("name").getAsString().equals(project)){
                            Integer id = element.get("id").getAsInt();
                            return id;
                        }
                    }
                }
            } else {
                System.out.print("AAAAAAAAAAAAAAAAAA");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void putInfo(String name,String subject){
        Project p = projectRep.findByNameAndSubject(name,subject);
        Integer id = getExternalId(p.getName());
        JsonObject root = new JsonObject();
        JsonObject json = new JsonObject();
        JsonObject identities = new JsonObject();
        identities.addProperty("GITHUB", p.getURL_github());
        //identities.addProperty("TAIGA",s.getUsername_taiga());
        //identities.addProperty("PRT",s.getUsername_sheets());

        json.add("identities",identities);

        root.add("data",json);

        System.out.println(root.toString());

        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        String body = gson.toJson(root);
        System.out.print(body);
        //RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", body)
                .build();
        try{
            Request putStudent = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/project/"+id))
                    .addHeader("Accept", "*/*")
                    .addHeader("Content-Type", "multipart/form-data")
                    .put(requestBody)
                    .build();

            Response postResponse = client.newCall(putStudent).execute();
            System.out.println(postResponse.body().string());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Integer> getProjectsSubject(String subject){
        List<Project> projects = projectRep.findAllBySubject(subject);
        List<Integer> ids = new ArrayList<>();

        for( Project p : projects){
            ids.add(getExternalId(p.getName()));
        }
        return ids;
    }

}
