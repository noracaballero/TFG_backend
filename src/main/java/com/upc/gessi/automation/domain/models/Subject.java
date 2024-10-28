package com.upc.gessi.automation.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "Subject")
public class Subject implements Serializable {

    @Id
    @Column(name ="name")
    private String name;

    @Column(name="te_github")
    private Boolean github;

    @Column(name="token_github")
    private String token_github;

    @Column(name ="te_taiga")
    private Boolean taiga;

    @Column(name = "te_sheets")
    private Boolean sheets;

    @Column(name = "username")
    private String username;

    public Subject() {

    }
    public Subject(String name, Boolean github, String token, Boolean taiga, Boolean sheets,String username){
        this.name = name;
        this.github = github;
        this.token_github= token;
        this.taiga = taiga;
        this.sheets = sheets;
        this.username = username;
    }

    public String getName() {
        return name;
    }
    public String getTokenGithub(){
        return token_github;
    }
    public Boolean getTaiga(){
        return taiga;
    }
    public Boolean getGithub(){
        return github;
    }
    public Boolean getSheets(){
        return sheets;
    }

    public String getUsername(){ return username;}


}
