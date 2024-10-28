package com.upc.gessi.automation.domain.controllers;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.gson.Gson;
import com.upc.gessi.automation.domain.controllers.config.QrConnectController;
import com.upc.gessi.automation.domain.controllers.config.QrEvalController;
import com.upc.gessi.automation.domain.models.Configuration;
import com.upc.gessi.automation.domain.respositories.ConfigurationRepository;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import java.io.*;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Instant;
import java.util.ArrayList;


@Controller
public class ConfigurationController {



    @Autowired
    ProjectController projectController;

    @Autowired
    QrConnectController connectController;

    @Autowired
    QrEvalController qrevalController;

    @Autowired
    StudentController studentController;

    @Autowired
    SubjectController subjectController;

    @Autowired
    ConfigurationRepository configurationRep;


    public void configure_connect(String name, String subject) throws IOException {

        if(itsConfig(name,subject,"github")){
            System.out.print("JA esta config");
        }
        else {

            Boolean github = subjectController.getGithub(subject);
            Boolean taiga = subjectController.getTaiga(subject);
            Boolean sheets = subjectController.getSheets(subject);

            if (github) {
                String github_name = projectController.getIdGithub(name, subject);
                connectController.config_GTS_properties(name, subject, "github", github_name);
                connectController.config_mongo_properties(name, subject, "github");
                connectController.configure_script(name, subject, "github");
            }
            if (taiga) {
                String taiga_name = projectController.getIdTaiga(name, subject);
                connectController.config_GTS_properties(name, subject, "taiga", taiga_name);
                connectController.config_mongo_properties(name, subject, "taiga");
                connectController.configure_script(name, subject, "taiga");
            }
            if (sheets) {
                String sheets_name = projectController.getIdSheets(name, subject);
                connectController.config_GTS_properties(name, subject, "sheets", sheets_name);
                connectController.config_mongo_properties(name, subject, "sheets");
                connectController.configure_script(name, subject, "sheets");
            }
            //projectController.setConfig(name, subject);
        }

    }

    public void configure_qreval(String name, String subject) throws IOException, InterruptedException {
        String path_new = "/home/qreval/run/projects/"+subject+"_"+name;
        File destination = new File(path_new);
        if (!destination.exists()) {
            Integer id_proj =projectController.getId(name,subject);
            ArrayList<String> usernames_github = studentController.getStudentsGithubProject(id_proj);
            ArrayList<String> usernames_taiga = studentController.getStudentsTaigaProject(id_proj);
            qrevalController.configure_eval_script(name,subject);
            qrevalController.getEvalProjects(name,subject,id_proj,usernames_github,usernames_taiga);
            qrevalController.createFolderProject(name,subject);
        }


    }

    public Boolean itsConfig(String name, String subject,String type) throws IOException {
        String path = "home/connect/run/config/"+type+"_" + subject+"/"+type+".properties";
        String searchTerm = subject+"_"+name;

        BufferedReader reader = new BufferedReader(new FileReader(path));
        Boolean found = false;

        String line;
        while ((line = reader.readLine()) != null) {
            // Buscar la palabra en la línea actual
            if (line.contains(searchTerm)) {
                found = true;
                
                break;
            }
        }
        return found;

    };

    public Integer createConfiguration(Integer number_project){
        Instant time = Instant.now();
        Configuration c = new Configuration(time.toString(),0,1+number_project*5);
        Configuration saveConfig = configurationRep.save(c);
        return saveConfig.getId();
    }

    public Integer getActualStatus(Integer id){
        Configuration conf = configurationRep.findById(id).orElse(null);
        return conf.getStatus();
    }

    public Boolean importData(){
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try{
            Request importProject = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/projects/import"))
                    .build();

            Response responseproject = client.newCall(importProject).execute();
            System.out.print("GET IMPORT PROJECTS");
            System.out.println(responseproject.body().string());

            Request importMetrics = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/metrics/import"))
                    .build();

            Response  responseMetrics= client.newCall(importMetrics).execute();
            System.out.print("GET IMPORT METRICS");
            System.out.println(responseMetrics.body().string());

            Request importFactors = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/qualityFactors/import"))
                    .build();

            Response  responseFactors= client.newCall(importFactors).execute();
            System.out.print("GET IMPORT FACTORS");
            System.out.println(responseFactors.body().string());

            Request importStrategic = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/strategicIndicators/fetch "))
                    .build();

            Response  responseStrategic= client.newCall(importStrategic).execute();
            System.out.print("GET IMPORT INDICATORS");
            System.out.println(responseStrategic.body().string());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
