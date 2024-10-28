package com.upc.gessi.automation.domain.controllers;

import com.upc.gessi.automation.domain.models.Subject;
import com.upc.gessi.automation.domain.respositories.SubjectRepository;
import com.upc.gessi.automation.rest.DTO.SubjectDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class SubjectControllerTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectController subjectController;

    @Test
    void getAll(){
        Subject s = new Subject("test",true,"123",true,false,"user");
        List<Subject> subjects = new ArrayList<>();
        subjects.add(s);
        Mockito.when(subjectRepository.findAll()).thenReturn(subjects);
        List<Subject> expected = subjectController.getAll();
        assertEquals(subjects,expected);
    }

    @Test
    void getToken(){
        SubjectDTO so = new SubjectDTO("test",true,"123",true,false,"user");
        Subject s = new Subject("test",true,"123",true,false,"user");
        Mockito.when(subjectRepository.save(any(Subject.class))).thenReturn(s);
        Mockito.when(subjectRepository.findByName(eq("test"))).thenReturn(s);

        subjectController.create(so);
        String token = subjectController.getTokenGit("test");
        assertEquals("123",token);
    }

    @Test
    void getGithub(){
        SubjectDTO so = new SubjectDTO("test",true,"123",true,false, "test");
        Subject s = new Subject("test",true,"123",true,false,"test");
        Mockito.when(subjectRepository.save(any(Subject.class))).thenReturn(s);
        Mockito.when(subjectRepository.findByName(eq("test"))).thenReturn(s);

        subjectController.create(so);
        Boolean result = subjectController.getGithub("test");
        assertEquals(true,result);
    }

    @Test
    void getTaiga(){
        SubjectDTO so = new SubjectDTO("test",true,"123",true,false,"test");
        Subject s = new Subject("test",true,"123",true,false,"test");
        Mockito.when(subjectRepository.save(any(Subject.class))).thenReturn(s);
        Mockito.when(subjectRepository.findByName(eq("test"))).thenReturn(s);

        subjectController.create(so);
        Boolean result = subjectController.getTaiga("test");
        assertEquals(true,result);
    }

    @Test
    void getSheets(){
        SubjectDTO so = new SubjectDTO("test",true,"123",true,false,"user");
        Subject s = new Subject("test",true,"123",true,false,"user");
        Mockito.when(subjectRepository.save(any(Subject.class))).thenReturn(s);
        Mockito.when(subjectRepository.findByName(eq("test"))).thenReturn(s);

        subjectController.create(so);
        Boolean result = subjectController.getSheets("test");
        assertEquals(false,result);
    }

}
