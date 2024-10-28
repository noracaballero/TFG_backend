package com.upc.gessi.automation.rest.DTO;

import com.upc.gessi.automation.domain.respositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentDTO  {

    private Integer id;
    private String name;
    private Integer project;
    private String username_github;
    private String username_taiga;
    private String username_sheets;


    private StudentRepository StudentRep;

    public StudentDTO() {

    }

    public StudentDTO(String name, Integer project, String username_github,String username_taiga,String username_sheets){
        this.name= name;
        this.project= project;
        this.username_github= username_github;
        this.username_taiga= username_taiga;
        this.username_sheets= username_sheets;
    }
    public StudentDTO(Integer id,String name, String username_github,String username_taiga,String username_sheets){
        this.id = id;
        this.name= name;
        this.username_github= username_github;
        this.username_taiga= username_taiga;
        this.username_sheets= username_sheets;
    }

    public Integer getId(){ return id;}

    public String getName(){
        return name;
    }

    public Integer getProject(){
        return project;
    }

    public String getUsername_github(){
        return username_github;
    }

    public String getUsername_taiga(){
        return username_taiga;
    }

    public String getUsername_sheets(){
        return username_sheets;
    }


}
