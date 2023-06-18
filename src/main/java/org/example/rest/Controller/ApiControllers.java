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
                && Strings.isNotBlank(user.getMail())) {
            if(!isUsernameAlreadyTook(user)) {
                if (handleRegistration(user)) {
                    return ResponseEntity.ok("User successfully registered !");
                }
            }
            return ResponseEntity.ok("Username is already took !");
        }
        return ResponseEntity.ok("An error was occured during registration !");
    }

    private boolean handleRegistration(User user) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        DSLContext context = DSL.using(db.getConnection(), SQLDialect.MYSQL);
        InsertValuesStep4<Record,Integer,String,String,String> insert = context
                .insertInto(table("users"))
                .columns(field("id",Integer.class), field("username",String.class), field("password",String.class), field("mail",String.class))
                .values(1, user.getUsername(), user.getPassword(), user.getMail());

        db.connect();
        db.setRequest(insert.getSQL());
        int numberRowsInjected = db.executeRegister(user);
        db.close();

        return numberRowsInjected == 1;
    }

    private boolean isUsernameAlreadyTook(User user) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();

        DSLContext context = DSL.using(SQLDialect.MYSQL);
        Table<Record> usersTable = table("users");
        Field<String> usernameField = field("username", String.class);

        SelectQuery<Record> selectQuery = context.selectQuery();
        selectQuery.addFrom(usersTable);
        selectQuery.addConditions(usernameField.eq(user.getUsername()));

        selectQuery.execute();
        db.connect();
        db.setRequest(selectQuery.getSQL());
        ResultSet result = db.verifyUsername(user);
        db.close();

        return result.wasNull();
    }
}
