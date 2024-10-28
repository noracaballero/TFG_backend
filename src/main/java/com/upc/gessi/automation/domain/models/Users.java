package com.upc.gessi.automation.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "Users")
public class Users implements Serializable {

    @Id
    @Column(name="username")
    private String username;

    @Column(name = "externalid")
    private Integer externalId;

    @Column(name = "password")
    private String password;

    public Users(String name, String password){
        this.username = name;
        this.password = password;
    }


    public Users() {

    }

    //public void setUsername(String username) {
        //this.username = username;
    //}

    public String getUsername() {
        return username;
    }

    public String getPassword(){ return password;}

    public Integer getExternalId(){ return externalId;}

    public void setExternalId(Integer id){
        this.externalId = id;
    }
}
