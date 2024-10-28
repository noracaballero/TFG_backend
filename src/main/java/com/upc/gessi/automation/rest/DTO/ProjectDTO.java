package com.upc.gessi.automation.rest.DTO;

public class ProjectDTO {

    private Integer Id;
    private String name;
    private String subject;
    private Integer num_students;
    private String urlGithub;
    private String urlTaiga;
    private String urlSheets;

    private String config_id;
    private Boolean config;

    public ProjectDTO(){};


    public ProjectDTO(String name, String subject, String urlGithub, String urlTaiga, String urlSheets){
        this.name=name;
        this.subject=subject;
        this.urlGithub=urlGithub;
        this.urlTaiga=urlTaiga;
        this.urlSheets=urlSheets;
    }
    public ProjectDTO(Integer Id,String name, String subject, String urlGithub, String urlTaiga, String urlSheets, Boolean config){
        this.Id=Id;
        this.name=name;
        this.subject=subject;
        this.urlGithub=urlGithub;
        this.urlTaiga=urlTaiga;
        this.urlSheets=urlSheets;
        this.config = config;

    }

    public Integer getId(){
        return Id;
    }
    public String getName(){
        return name;
    }

    public String getSubject(){
        return subject;
    }

    public String getUrlGithub(){
        return urlGithub;
    }

    public String getUrlTaiga(){
        return urlTaiga;
    }

    public String getUrlSheets(){
        return urlSheets;
    }

    public Boolean getConfig(){return config;}


}
