package com.upc.gessi.automation.domain.controllers;

import com.upc.gessi.automation.domain.models.Student;
import com.upc.gessi.automation.domain.respositories.StudentRepository;
import com.upc.gessi.automation.rest.DTO.StudentDTO;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Mock
    private StudentRepository studentRep;

    @InjectMocks
    private StudentController studentController;

    @Test
    void getAllTest(){
        List<String> names = new ArrayList<>();
        names.add("test1");
        names.add("test2");
        List<Student> s = new ArrayList<>();
        Student test = new Student("test1",1," "," "," ");
        Student test2 = new Student("test2",1," "," "," ");
        s.add(test);
        s.add(test2);
        when(studentRep.findAll()).thenReturn(s);
        List<String> expected = studentController.getAll();
        assertEquals(names,expected);

    }

    @Test
    void createStudentTest(){
        StudentDTO sDTO = new StudentDTO(1,"test"," ", " "," ");
        studentController.createStudent(sDTO);
        verify(studentRep, times(1)).save(any());
    }

    @Test
    void getUsernamesGithubTest(){
        List<String> names = new ArrayList<>();
        names.add("test1");
        names.add("test2");
        List<Student> s = new ArrayList<>();
        Student test = new Student("test1",1,"test1"," "," ");
        Student test2 = new Student("test2",1,"test2"," "," ");
        s.add(test);
        s.add(test2);
        when(studentRep.findAllByProject(1)).thenReturn(s);
        List<String> expected = studentController.getStudentsGithubProject(1);
        assertEquals(names.get(0),expected.get(0));
    }

    @Test
    void getUsernamesTaigaTest(){
        List<String> names = new ArrayList<>();
        names.add("test1");
        names.add("test2");
        List<Student> s = new ArrayList<>();
        Student test = new Student("test1",1,"test1","test1"," ");
        Student test2 = new Student("test2",1,"test2","test2"," ");
        s.add(test);
        s.add(test2);
        when(studentRep.findAllByProject(1)).thenReturn(s);
        List<String> expected = studentController.getStudentsTaigaProject(1);
        assertEquals(names.get(0),expected.get(0));
    }

    @Test
    void getUsernamesSheetsTest(){
        List<String> names = new ArrayList<>();
        names.add("test1");
        names.add("test2");
        List<Student> s = new ArrayList<>();
        Student test = new Student("test1",1,"test1","test1"," ");
        Student test2 = new Student("test2",1,"test2","test2"," ");
        s.add(test);
        s.add(test2);
        when(studentRep.findAllByProject(1)).thenReturn(s);
        List<String> expected = studentController.getStudentsSheetsProject(1);
        assertEquals(names.get(0),expected.get(0));
    }

    @Test
    void getStudentsProjectTest(){
        List<StudentDTO> names = new ArrayList<>();
        StudentDTO sDTO = new StudentDTO("test",1,"test1", " "," ");
        names.add(sDTO);
        List<Student> s = new ArrayList<>();
        Student test = new Student("test",1,"test1"," "," ");
        s.add(test);
        when(studentRep.findAllByProject(1)).thenReturn(s);
        List<StudentDTO> expected = studentController.getStudentsProject(1);
        assertEquals(names.get(0).getName(),expected.get(0).getName());
    }
}
