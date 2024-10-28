package com.upc.gessi.automation.rest.controllers;

import com.upc.gessi.automation.domain.controllers.UserController;
import com.upc.gessi.automation.rest.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    UserController userController;

    @GetMapping
    public List<UserDTO> getUsers(@RequestParam List<String> projects){
        List<UserDTO> users = new ArrayList<>();
        for(String s : projects){
            UserDTO u = userController.getUser(s);
             users.add(u);

        }
        return users;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<UserDTO> create(@RequestBody List<String> projects){
        List<UserDTO> users = new ArrayList<>();
        for(String name : projects) {
            System.out.println(name);
            String pssw = userController.createUser(name);
            UserDTO newUser = new UserDTO(name,pssw);
            users.add(newUser);


        }
        return users;
    }

    @PostMapping(value="/db")
    public void adddb() throws SQLException {
        userController.addUserProject("test3");
    }
}
