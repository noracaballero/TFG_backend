package com.upc.gessi.automation.domain.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table (name = "Student")
public class Student implements Serializable {

    @Id
    @Column (name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @SequenceGenerator(name="student_seq", sequenceName="student_id_seq", allocationSize=1)
    private Integer id;

    @Column (name = "name")
    private String name;

    @Column (name = "project")
    private Integer project;

    @Column (name = "username_github")
    private String username_github;

    @Column (name = "username_taiga")
    private String username_taiga;

    @Column (name = "username_sheets")
    private String username_sheets;

    public Student(String name, Integer project, String username_github,String username_taiga,String username_sheets){
        this.name= name;
        this.project= project;
        this.username_github= username_github;
        this.username_taiga= username_taiga;
        this.username_sheets= username_sheets;
    }

    public Student() {

    }
    public Integer getId() {return id;}
    public String getName() {
        return name;
    }

    public void setName(String name){ this.name = name;}

    public String getUsername_github(){
        return username_github;
    }

    public void setUsername_github(String username_github){
        this.username_github = username_github;
    }

    public String getUsername_taiga(){
        return username_taiga;
    }
    public void setUsername_taiga(String username_taiga){
        this.username_taiga = username_taiga;
    }

    public String getUsername_sheets(){
        return username_sheets;
    }

    public void setUsername_sheets(String username_sheets){
        this.username_sheets = username_sheets;
    }

    public Integer getProject() {
        return project;
    }
}
