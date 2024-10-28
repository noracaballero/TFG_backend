package com.upc.gessi.automation.rest.DTO;

public class SubjectDTO {

    private String name;

    private Boolean github;

    private String token_github;

    private Boolean taiga;

    private Boolean sheets;

    private String username;

    public SubjectDTO(String name, Boolean github, String token, Boolean taiga, Boolean sheets,String username){
        this.name = name;
        this.github = github;
        this.token_github = token;
        this.taiga = taiga;
        this.sheets = sheets;
        this.username = username;
    }

    public String getName(){
        return name;
    }

    public Boolean getGithub(){ return github;}

    public String getToken_github(){ return token_github;}

    public Boolean getTaiga(){ return taiga;}

    public Boolean getSheets(){ return sheets;}

    public String getUsername(){ return username;}

}
