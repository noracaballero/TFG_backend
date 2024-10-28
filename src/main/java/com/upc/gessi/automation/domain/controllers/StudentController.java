package com.upc.gessi.automation.domain.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.upc.gessi.automation.domain.models.Project;
import com.upc.gessi.automation.domain.respositories.StudentRepository;
import com.upc.gessi.automation.rest.DTO.ProjectDTO;
import com.upc.gessi.automation.rest.DTO.StudentDTO;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.upc.gessi.automation.domain.models.Student;

import java.net.URL;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository StudentRep;

    @Autowired
    private MetricController metricController;

    @Autowired
    private ProjectController projectController;

    public List<String> getAll(){
        Iterable<Student> studentsIt = StudentRep.findAll();
        studentsIt.forEach(student -> System.out.println(student));
        List<String> studentsList = new ArrayList<>();
        studentsIt.forEach(student -> studentsList.add(student.getName()));
        return studentsList;
    }

    public void createStudent(StudentDTO sDTO){
        Student student = new Student(sDTO.getName(),sDTO.getProject(),sDTO.getUsername_github(),sDTO.getUsername_taiga(),sDTO.getUsername_sheets());
        StudentRep.save(student);
    }

    public ArrayList<String> getStudentsGithubProject(Integer id_proj){
        List<Student> students = StudentRep.findAllByProject(id_proj);
        ArrayList<String> github_usernames = new ArrayList<>();
        for(Student student : students){
            System.out.print(student.getUsername_github());
            github_usernames.add(student.getUsername_github());
        }
        return github_usernames;
    }

    public ArrayList<String> getStudentsTaigaProject(Integer id_proj){
        List<Student> students = StudentRep.findAllByProject(id_proj);
        ArrayList<String> taiga_usernames = new ArrayList<>();
        for(Student student : students){
            System.out.print(student.getUsername_taiga());
            taiga_usernames.add(student.getUsername_taiga());
        }
        return taiga_usernames;
    }

    public ArrayList<String> getStudentsSheetsProject(Integer id_proj){
        List<Student> students = StudentRep.findAllByProject(id_proj);
        ArrayList<String> sheets_usernames = new ArrayList<>();
        for(Student student : students){
            System.out.print(student.getUsername_sheets());
            sheets_usernames.add(student.getUsername_sheets());
        }
        return sheets_usernames;
    }


    public List<StudentDTO> getStudentsProject(Integer id_proj){
        List<Student> students = StudentRep.findAllByProject(id_proj);
        List<StudentDTO> stu = new ArrayList<>();
        for(Student student : students){
            if(student.getProject() == id_proj) {
                StudentDTO studen = new StudentDTO(student.getId(), student.getName(), student.getUsername_github(), student.getUsername_taiga(), student.getUsername_sheets());
                stu.add(studen);
            }
        }
        return stu;
    }

    public void putStudents(String project){
        Project p = projectController.projectRep.findByName(project);
        Integer id = p.getId();
        List<Student> students = StudentRep.findAllByProject(id);
        for(Student s :students) {

            List<Integer> metricsList = metricController.getMetricStudents(id,s.getUsername_github(),s.getUsername_taiga(),s.getUsername_sheets());
            JsonObject json = new JsonObject();
            json.addProperty("id", "");
            json.addProperty("name",s.getName());

            JsonObject identities = new JsonObject();
            identities.addProperty("GITHUB", s.getUsername_github());
            identities.addProperty("TAIGA",s.getUsername_taiga());
            identities.addProperty("PRT",s.getUsername_sheets());

            json.add("identities",identities);

            JsonArray metrics = new JsonArray();

            for(Integer m : metricsList){
                metrics.add(m.toString());
            }
            json.add("metrics",metrics);
            System.out.println(json.toString());

            OkHttpClient client = new OkHttpClient();
            HttpClient httpClient = HttpClient.newHttpClient();
            Gson gson = new Gson();

            String body = gson.toJson(json);
            System.out.print(body);
            RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));
            try{
                Request putStudent = new Request.Builder()
                        .url(new URL("http://host.docker.internal:8888/api/metrics/students?prj="+project))
                        .addHeader("Accept", "*/*")
                        .put(requestBody)
                        .build();

                Response postResponse = client.newCall(putStudent).execute();
                System.out.println(postResponse.body().string());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }



        }
    }

    public void updateStudent(StudentDTO sDTO){
        Student s = StudentRep.findById(sDTO.getId()).orElse(null);
        s.setName(sDTO.getName());
        s.setUsername_github(sDTO.getUsername_github());
        s.setUsername_taiga(sDTO.getUsername_taiga());
        s.setUsername_sheets(sDTO.getUsername_sheets());
        StudentRep.save(s);
    }
}
