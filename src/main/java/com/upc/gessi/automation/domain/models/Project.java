package com.upc.gessi.automation.domain.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Project")
public class Project implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_seq")
    @SequenceGenerator(name="project_seq", sequenceName="project_id_seq", allocationSize=1)
    private Integer id;


    @Column(name= "name", unique = true)
    private String name;

    @Column(name= "subject")
    private String subject;

    @Column(name= "num_students")
    private Integer num_students;

    @Column(name= "URL_github")
    private String URL_github;

    @Column(name= "URL_taiga")
    private String URL_taiga;

    @Column(name= "URL_sheets", nullable = true)
    private String URL_sheets;

    @Column(name= "config_id")
    private Integer config_id;

    @Column(name = "config")
    private Boolean config;





    public Project(String name, String subject, String URL_github, String URL_taiga, String URL_sheets){
        this.name= name;
        this.subject= subject;
        this.URL_github=URL_github;
        this.URL_taiga=URL_taiga;
        this.URL_sheets=URL_sheets;
    }

    public Project() {

    }

    public Integer getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getSubject(){
        return subject;
    }

    public Integer getNum_students(){ return num_students;}
    public String getURL_github(){
        return URL_github;
    }

    public void setURL_github(String git){ this.URL_github = git;}
    public String getURL_taiga(){
        return URL_taiga;
    }
    public void setURL_taiga(String taiga){ this.URL_taiga = taiga;}
    public String getURL_sheets(){
        return URL_sheets;
    }
    public void setURL_sheets(String sheets){ this.URL_sheets = sheets;}

    public Boolean getConfig(){return config;}

    public void setConfig(Boolean status){
        this.config = status;
    }

    public String getID_github(){
        String[] parts = URL_github.split("/");

        int index=-1;
        for(int i = 0; i< parts.length; i++){
            if(parts[i].equals("github.com")){
                index=i;
                break;
            }
        }
        if(index != -1){
            System.out.print(parts[index+1]);
            return parts[index+1];
        }
        return null;
    }

    public String getID_taiga(){
        String[] parts = URL_taiga.split("/");

        int index=-1;
        for(int i = 0; i< parts.length; i++){
            if(parts[i].equals("project")){
                index=i;
                break;
            }
        }
        if(index != -1 && index + 1 < parts.length){
            return parts[index+1];
        }
        return null;
    }

    public String getID_Sheets(){
        String[] parts = URL_sheets.split("/");

        int index=-1;
        for(int i = 0; i< parts.length; i++){
            if(parts[i].equals("d")){
                index=i;
                break;
            }
        }
        if(index != -1 && index + 1 < parts.length){
            return parts[index+1];
        }
        return null;
    }


    public void setConfig_id(Integer num){
        this.config_id=num;
    }
    public void setNum_students(Integer num){this.num_students=num;}
    public Integer getConfig_id(){
        return config_id;
    }


    public void setId(int i) {
        this.id = i;
    }
}
