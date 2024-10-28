package com.upc.gessi.automation.rest.DTOs;

import com.upc.gessi.automation.rest.DTO.ProjectDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProjectDTOTest {

    private ProjectDTO projectDTO;

    @BeforeEach
    void setUp(){
        projectDTO = new ProjectDTO(1,"test","subject","url_github","url_taiga","url_sheets",true);
    }

    @Test
    void getIdTest(){
        assertEquals(1,projectDTO.getId());
    }

    @Test
    void getNameTest(){ assertEquals("test",projectDTO.getName());}

    @Test
    void getSubjectTest(){ assertEquals("subject",projectDTO.getSubject());}

    @Test
    void getUrlGithubTest(){ assertEquals("url_github",projectDTO.getUrlGithub());}

    @Test
    void getUrlTaiga(){ assertEquals("url_taiga",projectDTO.getUrlTaiga());}

    @Test
    void getUrlSheets(){ assertEquals("url_sheets",projectDTO.getUrlSheets());}
}
