package com.upc.gessi.automation.domain.controllers;

import com.google.gson.*;
import com.upc.gessi.automation.domain.models.Factor;
import com.upc.gessi.automation.domain.models.Project;
import com.upc.gessi.automation.domain.respositories.FactorRepository;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static okhttp3.RequestBody.create;

@Controller
public class FactorController {

    @Autowired
    FactorRepository factorRepository;

    @Autowired
    MetricController metricController;

    @Autowired
    ProjectController projectController;

    /*public Boolean existsFactor(String project,String name){
        List<Factor> factors = factorRepository.findAllByProject(project);
        if(factors.size() == 0) return false;
        else{
            for(Factor f: factors){
                if(f.getExternalid().equals(name)) return true;
            }
        }
        return false;
    }*/

    public void createFactors(String project){
        Project p = projectController.projectRep.findByName(project);
        Integer num_students = p.getNum_students();
        Boolean Sheets = false;


        if(!existsFactor(project,"commitsmanagement")) {
            //Commits Managment
            String desc = "Groups the metrics that measure the percentage of 'anonymous' commits, i.e. commits from unknown users which don't match with any known contributor.";
            Factor f = new Factor("commitsmanagement", "Commits Management", desc, "Github", "Reversed Default", project);
            factorRepository.save(f);

            //Commits Tasks Relation
            desc = "Percentage of commits with references to tasks with respect to the total number of commits in the project, i.e. commits with the word \"task #X\" in them, where X is the task number in Taiga.";
            f = new Factor("commitstasksrelation", "Commits Tasks Relation", desc, "Github", "Default", project);
            factorRepository.save(f);

            //Modified Lines Contribuition
            desc = "Groups the metrics that measure the percentage of modified lines of code made by a student with respect to the total number of modified lines of code in the project. Note: Both additions and deletions are considered as modified lines.";
            f = new Factor("modifiedlinescontribution", "Modified Lines Contribution", desc, "Github", "NoCategory", project);
            factorRepository.save(f);

            //Commits Contribution
            desc = "Groups the metrics that measure the percentage of commits made by a student with respect to the total number of commits of the project. Note: Merge commits are not taken into account.";
            f = new Factor("commitscontribution", "Commits Contribution", desc, "Github", "NoCategory", project);
            factorRepository.save(f);

            //Fullfillment of Tasks
            desc = "Groups the metrics that measure the percentage of closed tasks made by a student with respect to the total number of tasks assigned to this student.";
            f = new Factor("fulfillmentoftasks", "Fulfillment of Tasks", desc, "Taiga", "Default", project);
            factorRepository.save(f);

            //Tasks Contribution
            desc = "Groups the metrics that measure the percentage of tasks assigned to a student with respect to the total number of tasks in the project.";
            f = new Factor("taskscontribution", "Tasks Contribution", desc, "Taiga", num_students + " members contribution", project);
            factorRepository.save(f);

            //Task Effort Information
            desc = "Groups the metrics that measure tasks effort information, both estimated and actual effort to raise awareness of the importance of accuracy in estimating the time spent on different sprint tasks.";
            f = new Factor("taskseffortinformation", "Tasks Effort Information", desc, "Taiga", "Default", project);
            factorRepository.save(f);

            //Unassigned Tasks
            desc = "Measures the percentage of tasks without assignee with respect to the total number of tasks defined in this sprint.";
            f = new Factor("unassignedtasks", "Unassigned Tasks", desc, "Taiga", "Reversed Default", project);
            factorRepository.save(f);

            //User Stories Definition Quality
            desc = "Groups the metrics that measure the quality of user stories through the definition of acceptance criteria and the use of the specific pattern.";
            f = new Factor("userstoriesdefinitionquality", "User Stories Definition Quality", desc, "Taiga", "Default", project);
            factorRepository.save(f);

            //Deviation Metrics
            desc = "Groups the metrics that measure the standard deviation of different entities, more specifically commits, tasks or task effort estimation.";
            f = new Factor("deviationmetrics", "Deviation Metrics", desc, "", "Reversed Default", project);
            factorRepository.save(f);

            if (Sheets) {
                //Dedication Contribution
                desc = "Groups the metrics that measure the percentage of work hours dedicated to the project by a student with respect to the total number of hours in the project.";
                f = new Factor("dedicationcontribution", "Dedication Contribution", desc, "PRT", num_students + " members contribution", project);
                factorRepository.save(f);

                //Activity Distribution
                desc = "Groups the metrics that measure the percentage of work hours dedicated to a certain activity with respect to the number of hours in the project.";
                f = new Factor("activitydistribution", "Activity Distribution", desc, "PRT", "NoCategory", project);
                factorRepository.save(f);
            }
            postFactor(project);
        }

    }

    public void postFactor(String project){
        Integer num = projectController.getNumStudents(project);
        System.out.println("REAL" +num);
        //createFactors(project,num,false);
        System.out.println("ENTRA_POST_FACTOR");
        List<Factor> factors= factorRepository.findAllByProject(project);
        for(Factor f : factors){
            System.out.println(f.getName());
            if(!existsCategory(f.getCategory())){
                System.out.print("entraaa if not exists \n");
                createFactorCategory(f.getCategory());
            }
            List<Integer> metrics = metricController.getMetricsFactor(project,f.getExternalid());
            String metricsString = metrics.stream()
                            .map(String::valueOf)
                                    .collect(Collectors.joining(","));
            System.out.print("\n "+metricsString+"\n");
            System.out.print(metrics+"\n");

            OkHttpClient client = new OkHttpClient();
            HttpClient httpClient = HttpClient.newHttpClient();
            Gson gson = new Gson();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    //.addFormDataPart("prj",project)
                    .addFormDataPart("name",f.getName())
                    .addFormDataPart("description",f.getDescription())
                    .addFormDataPart("category",f.getCategory())
                    .addFormDataPart("threshold","")
                    .addFormDataPart("metrics", metricsString)
                    .addFormDataPart("type",f.getSource())
                    .build();

            try{
                Request postCategory = new Request.Builder()
                        .url(new URL("http://host.docker.internal:8888/api/qualityFactors?prj="+project))
                        .addHeader("Accept", "*/*")
                        .post(requestBody)
                        .build();

                Response postResponse = client.newCall(postCategory).execute();
                System.out.println("POSTFACTOR_LD");
                System.out.println(postResponse.body().string());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
    public Boolean existsCategory(String category){
        System.out.print("entra exists  "+category);
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        Boolean found = false;

        try {
            Request getRequest = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/factors/categories?name="+category))
                    .build();

            Response getResponse = client.newCall(getRequest).execute();
            if (getResponse.isSuccessful() ) {
                String data = getResponse.body().string();
                System.out.print(data);
                if (data != null && !data.equals("[]")) {
                    return true;
                }
            }
            else {
                System.out.print("AAAAAAAAAAAAAAAAAA");
            }
            return found;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsFactor(String project,String factor){
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        Boolean found = false;

        try {
            Request getRequest = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/qualityFactors?prj="+project))
                    .build();

            Response getResponse = client.newCall(getRequest).execute();
            if (getResponse.isSuccessful() ) {

                ResponseBody data = getResponse.body();
                String dataString = data.string();
                JsonArray factors = JsonParser.parseString(dataString).getAsJsonArray();
                for(JsonElement e : factors){
                    JsonObject obj = e.getAsJsonObject();
                    if(obj.has(factor)){
                        return true;
                    }
                }
            }
            else {
                System.out.print("AAAAAAAAAAAAAAAAAA");
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return found;
    }

    public void createFactorCategory(String category){
        System.out.print(category);
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        String[] upperThreshold ={};
        String[] color ={};
        String[] type={};

        if(category.equals("Default")){
            upperThreshold = new String[]{"100", "67", "33"};
            color = new String[]{"#00ff00", "#ff8000", "#ff0000"};
            type= new String[]{"Good", "Neutral", "Bad"};
        }
        else if(category.equals("Reversed Default")){
            upperThreshold = new String[]{"100", "67", "33"};
            color = new String[]{"#ff0000", "#ff8000", "#00ff00"};
            type= new String[]{"Bad", "Neutral", "Good"};
        }
        else if(category.equals("NoCategory")){
            upperThreshold = new String[]{"100", "0", "0"};
            color = new String[]{"#9eacd6", "#ff8000", "#ff0000"};
            type= new String[]{"Neutral", "N", "Bad"};
        }
        else if(category.contains("members contribution")){
            if(category.contains("4")){
                upperThreshold = new String[]{"100", "40", "30","20","10"};
            } else if (category.contains("5")) {
                upperThreshold = new String[]{"100", "35", "25","15","5"};
            } else if (category.contains("3")) {
                upperThreshold = new String[]{"100", "48", "38","28","10"};
            } else if (category.contains("6")) {
                upperThreshold = new String[]{"100", "30", "20","10","5"};
            } else if (category.contains("7")) {
                upperThreshold = new String[]{"100", "32", "22","12","7"};
            } else if (category.contains("8")) {
                upperThreshold = new String[]{"100", "30", "20","10","6"};
            }
            color = new String[]{"#ff0000", "#ff8000", "#00ff00","#ff8000", "#ff0000"};
            type= new String[]{"High", "Up", "Well","Down","Low"};
        }
        List<JsonObject> jsonArray = new ArrayList<>();

        for(int i = 0; i<upperThreshold.length; i++){
            JsonObject obj = new JsonObject();
            obj.addProperty("upperThreshold",upperThreshold[i]);
            obj.addProperty("color",color[i]);
            obj.addProperty("type",type[i]);
            jsonArray.add(obj);
        }
        System.out.print("asasasasasa");
        System.out.print(jsonArray);

        //String requestBodyJson = jsonArray.toString();
        String requestBodyJson = gson.toJson(jsonArray);

        System.out.print(requestBodyJson);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),requestBodyJson);
        try{
            Request putCategory = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/factors/categories?name="+category))
                    .addHeader("Accept","application/json")
                    .addHeader("Accept", "*/*")
                    .post(requestBody)
                    .build();

            Response putResponse = client.newCall(putCategory).execute();
            System.out.println(putResponse.body().string());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
