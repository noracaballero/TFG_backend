package com.upc.gessi.automation.domain.controllers;

import com.google.gson.*;
import com.upc.gessi.automation.domain.models.Metric;
import com.upc.gessi.automation.domain.respositories.MetricRepository;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MetricController {

    @Autowired
    MetricRepository metricRepository;

    @Autowired
    ProjectController projectController;

    public void createMetric(Integer externalid, String name, String project) {
        if(!metricRepository.existsByExternalid(externalid)){
            Metric m = new Metric(externalid, name, project);
            metricRepository.save(m);
        }
    }

    public void addMetrics(String project){
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try {

            Request getRequestFactor = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/metrics?prj="+project))
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
                        System.out.print(element+"\n");
                        Integer id = element.get("id").getAsInt();
                        String externalid = element.get("externalId").getAsString();
                        System.out.print(externalid+"\n");
                        createMetric(id,externalid,project);
                    }
                }
            } else {
                System.out.print("AAAAAAAAAAAAAAAAAA");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setFactorMetric(String project) {
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try {

            Request getRequestFactor = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/metrics/current?prj=" + project))
                    .build();

            Response getResponseFact = client.newCall(getRequestFactor).execute();
            //HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            if (getResponseFact.isSuccessful()) {
                System.out.print("AAAAAAAAAA");
                ResponseBody data = getResponseFact.body();
                System.out.print(data);
                if (data != null) {
                    String dataString = data.string();
                    System.out.println(dataString);
                    JsonArray json = JsonParser.parseString(dataString).getAsJsonArray();
                    for (int i = 0; i < json.size(); i++) {
                        JsonObject element = json.get(i).getAsJsonObject();
                        System.out.print(element + "\n");
                        String externalid = element.get("id").getAsString();
                        System.out.print(externalid + "\n");
                        JsonArray qualityFactorsArray = element.getAsJsonArray("qualityFactors");
                        System.out.print("qualityFactorsArray  : ");
                        String factor = qualityFactorsArray.get(0).getAsString();
                        System.out.print(factor);
                        System.out.print("\n" + externalid + "  " + factor + "\n");
                        Metric met = metricRepository.findByNameAndProject(externalid, project);
                        System.out.print(met.getName());
                        met.setFactor(factor);
                        metricRepository.save(met);

                    }
                }
            } else {
                System.out.print("AAAAAAAAAAAAAAAAAA");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCategoryMetric(String project) {
        Integer num_students = projectController.getNumStudents(project);
        System.out.println(num_students);
        List<Metric> metrics = metricRepository.findAllByProject(project);

        for (Metric m : metrics) {
            System.out.println(m.getName());
            if (m.getName().startsWith("commits_anonymous") || m.getName().startsWith("commits_sd") || m.getName().startsWith("deviation_effort_estimation_simple") || m.getName().startsWith("tasks_sd") || m.getName().startsWith("unassignedtasks")) {
                if (!existsCategory("Reversed Default")) {
                    createMetricCategory("Reversed Default");
                }
                System.out.print("entra ifff");
                putCategory(m.getExternalid(), m.getProject(), "Reversed Default");
            } else if (m.getName().startsWith("commits_") || m.getName().startsWith("modifiedlines_")) {
                if (!existsCategory("NoCategory")) {
                    createMetricCategory("NoCategory");
                }
                putCategory(m.getExternalid(), m.getProject(), "NoCategory");
            } else if (m.getName().startsWith("assignedtasks_")) {
                if (!existsCategory(num_students + " members contribution")) {
                    createMetricCategory(num_students + " members contribution");
                }
                putCategory(m.getExternalid(), m.getProject(), num_students + " members contribution");
            }
        }
        System.out.println("PLEASEEEEEEE");
    }

    public void putCategory(Integer id, String project, String Category) {
        System.out.println("OIOIOIOIOIO");
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("categoryName", Category)
                .addFormDataPart("threshold", "")
                .addFormDataPart("url", "")
                .build();
        System.out.println("PATATATATATATATAT");

        try {
            Request putCategory = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/metrics/" + id + "?prj=" + project))
                    .addHeader("Accept", "*/*")
                    .put(requestBody)
                    .build();

            Response putResponse = client.newCall(putCategory).execute();
            System.out.println("PUTCATEGORY");
            System.out.println(putResponse.body().string());

        } catch (Exception e) {
            System.err.println("Error in putCategory method for ID: " + id + " and project: " + project);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Boolean existsCategory(String category) {
        System.out.println("EXISTS_CATEGORY");
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        Boolean found = false;

        try {
            Request getRequest = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/metrics/list"))
                    .build();

            Response getResponse = client.newCall(getRequest).execute();
            System.out.println("EXISTS_CATEGORY");
            if (getResponse.isSuccessful()) {
                ResponseBody data = getResponse.body();
                if (data != null) {
                    String dataString = data.string();
                    JsonArray json = JsonParser.parseString(dataString).getAsJsonArray();
                    for (JsonElement elemnt : json) {
                        if (category == elemnt.getAsString()) {
                            return true;
                        } else {
                            found = false;
                        }
                    }
                }
            } else {
                System.out.print("AAAAAAAAAAAAAAAAAA");
            }
            return found;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createMetricCategory(String category) {
        System.out.println("CREATE_CATEGORY");
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();
        RequestBody requestBody = null;
        String[] upperThreshold = {};
        String[] color = {};
        String[] type = {};

        if (category == "Default") {
            upperThreshold = new String[]{"100", "67", "33"};
            color = new String[]{"#00ff00", "#ff8000", "#ff0000"};
            type = new String[]{"Good", "Neutral", "Bad"};
        } else if (category == "Reversed Default") {
            upperThreshold = new String[]{"100", "67", "33"};
            color = new String[]{"#ff0000", "#ff8000", "#00ff00"};
            type = new String[]{"Bad", "Neutral", "Good"};
        } else if (category == "NoCategory") {
            upperThreshold = new String[]{"100", "0", "0"};
            color = new String[]{"#9eacd6", "#ff8000", "#ff0000"};
            type = new String[]{"Neutral", "Low", "Bad"};
        } else if (category.contains("members contribution")) {
            if (category.contains("4")) {
                upperThreshold = new String[]{"100", "40", "30", "20", "10"};
            } else if (category.contains("5")) {
                upperThreshold = new String[]{"100", "35", "25", "15", "5"};
            } else if (category.contains("3")) {
                upperThreshold = new String[]{"100", "48", "38", "28", "10"};
            } else if (category.contains("6")) {
                upperThreshold = new String[]{"100", "30", "20", "10", "5"};
            } else if (category.contains("7")) {
                upperThreshold = new String[]{"100", "32", "22", "12", "7"};
            } else if (category.contains("8")) {
                upperThreshold = new String[]{"100", "30", "20", "10", "6"};
            }
            color = new String[]{"#ff0000", "#ff8000", "#00ff00", "#ff8000", "#ff0000"};
            type = new String[]{"High", "Up", "Well", "Down", "Low"};
        }
        List<JsonObject> jsonArray = new ArrayList<>();

        for (int i = 0; i < upperThreshold.length; i++) {
            JsonObject obj = new JsonObject();
            obj.addProperty("upperThreshold", upperThreshold[i]);
            obj.addProperty("color", color[i]);
            obj.addProperty("type", type[i]);
            jsonArray.add(obj);
        }

        String requestBodyJson = gson.toJson(jsonArray);
        requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson);
        try {
            Request putCategory = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/metrics/categories?name=" + category))
                    //.addHeader("Accept","application/json")
                    .addHeader("Accept", "*/*")
                    .put(requestBody)
                    .build();

            Response putResponse = client.newCall(putCategory).execute();
            System.out.println("CREATE METRIC CATEGORY");
            System.out.println(putResponse.body().string());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Integer> getMetricsFactor(String project, String factor) {
        List<Integer> ids = new ArrayList<>();
        List<Metric> metrics = metricRepository.findAllByProject(project);
        for (Metric m : metrics) {
            System.out.print("ENTRAAAAAAAA");
            System.out.print(m.getFactor());
            System.out.print(" " + factor);
            if (m.getFactor().equals(factor)) {
                System.out.print("sasasasasasa\n");
                ids.add(m.getExternalid());
                ids.add(-1);
            }
        }
        return ids;
    }

    public List<Integer> getMetricStudents(Integer id, String username_git, String username_taiga, String username_sheets) {
        String project = projectController.getName(id);
        List<Metric> metrics = metricRepository.findAllByProject(project);
        List<Integer> metricStudent = new ArrayList<>();
        username_git = username_git.replaceAll("[^a-zA-Z]","_");
        username_taiga = username_taiga.replaceAll("[^a-zA-Z]","_");

        for (Metric m : metrics) {
            if (m.getName().contains(username_git) || m.getName().contains(username_taiga)){
                System.out.println(m.getName()+"  "+m.getExternalid());
                metricStudent.add(m.getExternalid());
            }
            if(username_sheets != null){
                username_sheets = username_sheets.replaceAll("[^a-zA-Z]","_");
                metricStudent.add(m.getExternalid());
            }
        }
        return metricStudent;
    }
}