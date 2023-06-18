package org.example.rest.ApiControllers;

import classes.User;
import database.DatabaseConnector;
import org.apache.logging.log4j.util.Strings;
import org.jooq.*;
import org.jooq.Record;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class RegisterApi {


    @GetMapping("/")
    @ResponseBody
    public String home() {

        return "home";
    }

    @PutMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) throws SQLException {

        if (Strings.isNotBlank(user.getUsername())
                && Strings.isNotBlank(user.getPassword())
                && Strings.isNotBlank(user.getMail())) {
            if (!isUsernameAlreadyTook(user)) {
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
        DatabaseConnector databaseConnection = new DatabaseConnector();
        InsertValuesStep4<Record, Integer, String, String, String> insert = databaseConnection.getContext()
                .insertInto(table("users"))
                .columns(field("id", Integer.class), field("username", String.class), field("password", String.class), field("mail", String.class))
                .values(null, user.getUsername(), BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()), user.getMail());
        int numberRowsInjected = insert.execute();
        databaseConnection.getDataSource().getConnection().close();

        return numberRowsInjected == 1;
    }

    private boolean isUsernameAlreadyTook(User user) throws SQLException {

        DatabaseConnector databaseConnection = new DatabaseConnector();
        SelectQuery<Record> query = databaseConnection.getContext().selectQuery();
        Table<Record> usersTable = table("users");
        Field<String> usernameField = field("username", String.class);
        query.addSelect(usersTable.fields());
        query.addFrom(usersTable);
        query.addConditions(usernameField.eq(user.getUsername()));
        // Execute the query
        Result<Record> result = query.fetch();
        databaseConnection.getDataSource().getConnection().close();
        return !result.isEmpty();
    }
}