package org.example.rest.Controller;

import classes.User;
import database.DatabaseConnector;
import org.apache.logging.log4j.util.Strings;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;


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
    public ResponseEntity<String> register( @RequestBody User user) throws SQLException {

        if (Strings.isNotBlank(user.getUsername())
                && Strings.isNotBlank(user.getPassword())
                && Strings.isNotBlank(user.getEmail())) {
            if(handleRegistration(user)) {
                System.out.println("OK");
            }
        } else {
            System.out.println("PAS OK");

        }

        return ResponseEntity.ok("Data updated successfully");
    }

    private boolean handleRegistration(User user) throws SQLException {
        DatabaseConnector db = new DatabaseConnector("nicolas","Ficellejulien66!");

        DSLContext context = DSL.using(db.getConnection(), SQLDialect.MYSQL);
        InsertValuesStep4<Record,Object,Object,Object,Object> select = context
                .insertInto(table("users"))
                .columns(field("id"), field("username"), field("password"), field("mail"))
                .values(null, user.getUsername(), user.getPassword(), user.getEmail());
        db.setRequest(select.getSQL());
        db.connect();
        ResultSet result = db.execute();
        while(result.next()){
            System.out.println(result.getString(1)+" "+result.getString(2));
        }
        db.close();
        return true;
    }
}
