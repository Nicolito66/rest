package org.example.rest.Controller;

import classes.User;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import database.DatabaseConnector;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.sql.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class ApiControllers {


    @GetMapping("/")
    @ResponseBody
    public String home() {

        return "home";
    }
//    @GetMapping("/register")
//    public String registration() throws SQLException {
//        DatabaseConnector db = new DatabaseConnector("nicolas","Ficellejulien66!");
//        db.setRequest("INSERT INTO `users` (`id`, `username`, `password`, `mail`) VALUES (NULL, 'a', 'b', 'c');");
//        db.connect();
//        ResultSet result = db.execute();
//        while(result.next()){
//            System.out.println(result.getString(1)+" "+result.getString(2));
//        }
//        db.close();
//
//
//        return "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
//    }

    @PutMapping("/register")
    public ResponseEntity<String> updateData( @RequestBody User user) {

        System.out.println("YES");
        if (user)

        return ResponseEntity.ok("Data updated successfully");
    }
}
