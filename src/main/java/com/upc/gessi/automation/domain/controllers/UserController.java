package com.upc.gessi.automation.domain.controllers;

import com.google.gson.*;
import com.upc.gessi.automation.domain.models.Users;
import com.upc.gessi.automation.domain.respositories.UserRepository;
import com.upc.gessi.automation.rest.DTO.UserDTO;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.sql.*;
import java.sql.Connection;
import java.util.Random;

@Controller
public class UserController {

    @Autowired
    UserRepository userRep;

    public UserDTO getUser(String project){
        Users u = userRep.findByUsername(project);
        System.out.println(u);
        UserDTO result = new UserDTO(u.getUsername(),u.getPassword());
        System.out.println(result);
        return result;
    }

    public String createUser(String name){
        if(!userRep.existsByUsername(name)){
            String password = generatePassword();
            Users u = new Users(name,password);
            userRep.save(u);
            OkHttpClient client = new OkHttpClient();
            HttpClient httpClient = HttpClient.newHttpClient();
            Gson gson = new Gson();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    //.addFormDataPart("prj",project)
                    .addFormDataPart("username",name)
                    .addFormDataPart("email",name + "@gmail.com")
                    .addFormDataPart("question"," ")
                    .addFormDataPart("password",password)
                    .build();

            try{
                Request postCategory = new Request.Builder()
                        .url(new URL("http://host.docker.internal:8888/signup"))
                        .addHeader("Accept", "*/*")
                        .post(requestBody)
                        .build();

                Response postResponse = client.newCall(postCategory).execute();
                System.out.println("CREATE_USER");
                System.out.println(postResponse.body().string());
                if(postResponse.isSuccessful()){
                    setExternalid();
                }
                return password;


            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }
        return null;

    }

    private String generatePassword(){
        Random random = new Random();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder pass = new StringBuilder(8);

        for(int i = 0; i< 8; ++i){
            int index = random.nextInt(chars.length());
            pass.append(chars.charAt(index));
        }
        return pass.toString();
    }

    public void addUserProject(String name) {
        Users u = userRep.findByUsername(name);
        Integer id_project = getIdProjectUser(name);

        String query = "INSERT INTO user_project values(" + u.getExternalId() + "," + id_project + ");";

        try {
            /*Connection connection = DriverManager.getConnection("jdbc:postgresql://db:5432/postgres","postgres","example");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("INSERT INTO user_project values("+u.getExternalId()+","+id_project+");");
*/

            Connection connection = DriverManager.getConnection("jdbc:postgresql://db:5432/postgres", "postgres", "example");
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            {

                // Configura los parámetros de la consulta
                preparedStatement.setInt(1, u.getExternalId());
                preparedStatement.setInt(2, id_project);

                // Ejecuta la consulta de inserción
                int rowsAffected = preparedStatement.executeUpdate();
            }

            /*while (resultSet.next()) {
                if(userRep.existsByUsername(resultSet.getString("username"))){
                    User u = userRep.findByUsername(resultSet.getString("username"));
                    u.setExternalId(resultSet.getInt("id"));
                    userRep.save(u);
                }
            }*/

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        private void setExternalid() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://db:5432/postgres","postgres","example");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appuser;");

            while (resultSet.next()) {
                if(userRep.existsByUsername(resultSet.getString("username"))){
                    Users u = userRep.findByUsername(resultSet.getString("username"));
                    u.setExternalId(resultSet.getInt("id"));
                    userRep.save(u);
                }
                //System.out.println("ID: " + id + ", Name: " + username );
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Integer getIdProjectUser(String name){
        OkHttpClient client = new OkHttpClient();
        HttpClient httpClient = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try {
            Request getRequest = new Request.Builder()
                    .url(new URL("http://host.docker.internal:8888/api/project"))
                    .build();

            Response getResponse = client.newCall(getRequest).execute();
            if (getResponse.isSuccessful() ) {
                System.out.print("AAAAAAAAAA");
                ResponseBody data = getResponse.body();
                System.out.print(data);
                if(data !=null) {
                    String dataString = data.string();
                    System.out.println(dataString);
                    JsonArray json = JsonParser.parseString(dataString).getAsJsonArray();
                    for(int i= 0;i<json.size(); i++){
                        JsonObject element = json.get(i).getAsJsonObject();
                        if(element.get("externalId").getAsString().equals(name)) {
                            Integer id = element.get("id").getAsInt();
                            return id;
                        }
                    }
                }
            } else {
                System.out.print("AAAAAAAAAAAAAAAAAA");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
