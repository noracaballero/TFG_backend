package com.upc.gessi.automation.domain.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.upc.gessi.automation.domain.models.Factor;
import com.upc.gessi.automation.domain.models.Iteration;
import com.upc.gessi.automation.domain.respositories.IterationRepository;
import com.upc.gessi.automation.rest.DTO.IterationDTO;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpClient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IterationController {

    @Autowired
    IterationRepository iterationRepository;

    @Autowired
    SubjectController subjectController;

    @Autowired
    ProjectController projectController;

    public Integer getExternalId(String name){
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try {

            Request getRequestFactor = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/iterations"))
                    .build();

            Response getResponseFact = client.newCall(getRequestFactor).execute();
            //HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            if (getResponseFact.isSuccessful() ) {
                System.out.print("AAAAAAAAAA");
                ResponseBody data = getResponseFact.body();
                System.out.print(data);
                if(data !=null) {
                    String dataString = data.string();
                    System.out.println(dataString);
                    JsonArray json = JsonParser.parseString(dataString).getAsJsonArray();
                    for(int i= 0;i<json.size(); i++){
                        JsonObject element = json.get(i).getAsJsonObject();
                        if(element.get("name").getAsString().equals(name)){
                            System.out.print(element.get("id").getAsInt());
                            return element.get("id").getAsInt();
                        }
                    }
                }
            } else {
                System.out.print("AAAAAAAAAAAAAAAAAA");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }

    public void addProject(String name, List<String> projects_names){
        List<Iteration> its = iterationRepository.findAllBySubject(name);
        List<Integer> id_project = new ArrayList<>();
        for(String p : projects_names){
            System.out.println(p);
            Integer id = projectController.getExternalId(p);
            id_project.add(id);
        }
        for(Iteration it : its) {
            Integer id_iteration = getExternalId(it.getName());
            System.out.println(it.getName());
            if (!id_iteration.equals(-1)) {
                System.out.println("entraoaooa");
                Iteration i = iterationRepository.findByName(it.getName());
                OkHttpClient client = new OkHttpClient();
                HttpClient httpClient = HttpClient.newHttpClient();
                Gson gson = new Gson();
                JsonObject rootObject = new JsonObject();
                JsonArray projects = new JsonArray();
                if(!i.getProjects().isEmpty()) {
                    System.out.print("entra not projects");
                    String[] projectsID = i.getProjects().split(",");
                    for (String id : projectsID) {
                        System.out.print(id);
                        projects.add(id.trim().replace("[","").replace("]",""));
                    }
                }
                for (Integer id : id_project) {
                    projects.add(id.toString());
                }
                rootObject.add("project_ids", projects);

                JsonObject iterationInfo = new JsonObject();
                iterationInfo.addProperty("name", it.getName());
                iterationInfo.addProperty("label", i.getSubject());
                iterationInfo.addProperty("fromDate", i.getFromData());
                System.out.print(i.getFromData());
                iterationInfo.addProperty("toDate", i.getToData());
                rootObject.add("iteration", iterationInfo);

                String body = gson.toJson(rootObject);
                System.out.println("AQUIII");
                System.out.println(body);
                RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));

                try {
                    Request postCategory = new Request.Builder()
                            .url(new URL("http://host.docker.internal:8888/api/iterations/" + id_iteration))
                            .addHeader("Accept", "*/*")
                            .put(requestBody)
                            .build();

                    Response postResponse = client.newCall(postCategory).execute();
                    System.out.println(postResponse.body().string());

                    if (postResponse.isSuccessful()) {
                        StringBuilder old = new StringBuilder(i.getProjects());
                        for (Integer id : id_project) {
                            if (old.length() > 0) {
                                old.append(",");
                            }
                            old.append(id);
                        }
                        i.setProjectIds(old.toString());
                        iterationRepository.save(i);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void addIteration(IterationDTO i) throws ParseException {
        SimpleDateFormat in = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd");
        System.out.print("aaaaaa");
        String name = i.getName();
        System.out.print(name);
        String subject = i.getSubject();
        System.out.print(subject);
        String from = i.getFromData();
        Date datafrom = in.parse(from);
        from = out.format(datafrom);
        System.out.print(from);
        String to = i.getToData();
        Date datato = in.parse(to);
        to = out.format(datato);
        System.out.print(to);

        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        String[] projectsID = i.getProjects().split(",");



        JsonObject rootObject = new JsonObject();
        JsonArray projects = new JsonArray();

        for(String id :projectsID){
            System.out.print(id);
            id = id.replace("[","");
            id = id.replace("]","");
            projects.add(id.trim());
        }
        rootObject.add("project_ids",projects);

        JsonObject iterationInfo = new JsonObject();
        iterationInfo.addProperty("name",name);
        iterationInfo.addProperty("label",subject);
        iterationInfo.addProperty("fromDate",from);
        iterationInfo.addProperty("toDate",to);
        rootObject.add("iteration",iterationInfo);

        String body = gson.toJson(rootObject);
        System.out.print(body);
        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));

        try{
            Request postCategory = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/iterations"))
                    .addHeader("Accept", "*/*")
                    .post(requestBody)
                    .build();

            Response postResponse = client.newCall(postCategory).execute();

            System.out.println(postResponse.body().string());

            if(postResponse.isSuccessful()){
                Iteration newIteration = new Iteration(i.getName(),i.getSubject(),i.getFromData(),i.getToData(),i.getProjects());
                iterationRepository.save(newIteration);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<IterationDTO> getIterationSub(String subject){
        List<IterationDTO> iters = new ArrayList<>();
        List<Iteration> its = iterationRepository.findAllBySubject(subject);
        for(Iteration it: its){
            IterationDTO dto = new IterationDTO(it.getName(),it.getSubject(),it.getFromData(),it.getToData(),it.getProjects());
            iters.add(dto);
        }
        return iters;
    }

}
