package com.upc.gessi.automation.rest.controllers;

import com.upc.gessi.automation.domain.controllers.MetricController;
//import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metrics")
public class MetricRestController {

    @Autowired
    MetricController metricController;

    @PostMapping(value="/category")
    public void putCategory(@RequestParam(name = "project")String project){
        metricController.addCategoryMetric(project);
    }

    /*@GetMapping(value="/stu")
    public List<Integer> getMet(){
        return metricController.getMetricStudents("bravo11","annaamc","annaamc","");
    }*/

    /*@GetMapping(value="/CA")
    public void newCategory(){
        metricController.createMetricCategory("Default");
    }*/



    @PostMapping
    public void configLD(@RequestBody List<String> projects){
        for(String p : projects) {
            System.out.println(p);
            System.out.print("ADD_METRICS");
            metricController.addMetrics(p);
            System.out.print("SET_FACTOR");
            metricController.setFactorMetric(p);
            System.out.print("ADD_CATEGORY_METRIC");
            metricController.addCategoryMetric(p);
            System.out.print("AAADAKJDKHDKDJHjhg");
        }

    }


    /*@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createMetrics(@RequestParam(name = "name") String name) throws IOException, InterruptedException, URISyntaxException {
        metricController.addMetrics(name);
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

    try {

        Request getRequestFactor = new Request.Builder()
                .url(new URL("http://host.docker.internal:8888/api/metrics/current?prj="+name))
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
                    String externalid = element.get("id").getAsString();
                    System.out.print(externalid+"\n");
                    JsonArray qualityFactorsArray = element.getAsJsonArray("qualityFactors");
                    System.out.print("qualityFactorsArray  : ");
                    String factor = qualityFactorsArray.get(0).getAsString();
                    System.out.print(factor);
                    System.out.print("\n"+ externalid+"  "+factor+"\n");
                    //metricController.setFactorMetric(externalid,factor,name);
                }
            }
        } else {
            System.out.print("AAAAAAAAAAAAAAAAAA");
        }
    }catch (IOException e){
        e.printStackTrace();
    }
        return "patata";
    };*/
}
