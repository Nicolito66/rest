package org.example.rest.Controller;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import database.DatabaseConnector;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@RestController
public class ApiControllers {
    @GetMapping("/")
    @ResponseBody
    public String getPage() throws SQLException {
        DatabaseConnector db = new DatabaseConnector("nicolas","Ficellejulien66!");
        db.setRequest("SELECT * FROM users");
        db.connect();
        ResultSet result = db.execute();
        while(result.next()){
            System.out.println(result.getString(1)+" "+result.getString(2));
        }
        db.close();


        return "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    }
}
