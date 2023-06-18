package org.example.rest.Controller;

import classes.User;
import database.DatabaseConnection;
import org.apache.logging.log4j.util.Strings;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("/register")
    public ResponseEntity<String> register( @RequestBody User user) throws SQLException {

        if (Strings.isNotBlank(user.getUsername())
                && Strings.isNotBlank(user.getPassword())
                && Strings.isNotBlank(user.getMail())) {
            if(!isUsernameAlreadyTook(user)) {
                if (handleRegistration(user)) {
                    return ResponseEntity.ok("User successfully registered !");
                }
                return ResponseEntity.ok("An error was occured during registration !");
            }
            return ResponseEntity.ok("Username is already took !");
        }
        return ResponseEntity.ok("Fields empty !");
    }

    private boolean handleRegistration(User user) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        InsertValuesStep4<Record,Integer,String,String,String> insert = databaseConnection.getContext()
                .insertInto(table("users"))
                .columns(field("id",Integer.class), field("username",String.class), field("password",String.class), field("mail",String.class))
                .values(1, user.getUsername(), user.getPassword(), user.getMail());
        int numberRowsInjected = insert.execute();

        return numberRowsInjected == 1;
    }

    private boolean isUsernameAlreadyTook(User user) throws SQLException {

            DatabaseConnection databaseConnection = new DatabaseConnection();
            SelectQuery<Record> query = databaseConnection.getContext().selectQuery();
            Table<Record> usersTable = table("users");
            Field<String> usernameField = field("username", String.class);
            query.addSelect(usersTable.fields());
            query.addFrom(usersTable);
            query.addConditions(usernameField.eq(user.getUsername()));
            // Execute the query
            Result<Record> result = query.fetch();

        return !result.isEmpty();
    }
}
