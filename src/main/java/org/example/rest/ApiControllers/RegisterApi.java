package org.example.rest.ApiControllers;

import classes.Response;
import classes.User;
import org.example.rest.database.DatabaseConnector;
import org.apache.logging.log4j.util.Strings;
import org.jooq.*;
import org.jooq.Record;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import static org.example.rest.ApiControllers.DatabaseUtils.insertEmptyCookie;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class RegisterApi {

    private final DatabaseConnector databaseConnection;

    @Autowired
    public RegisterApi(DatabaseConnector databaseConnector) {
        this.databaseConnection = databaseConnector;
    }

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "home";
    }

    @PutMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) throws SQLException {
        if (Strings.isNotBlank(user.getUsername())
                && Strings.isNotBlank(user.getPassword())
                && Strings.isNotBlank(user.getMail())) {
            if (!isUsernameOrEmailAlreadyTook(user, databaseConnection)) {
                if (handleRegistration(user)) {
                        DatabaseUtils.UpdateVerificationCode(user.getId(), databaseConnection,user.getMail());
                    return ResponseEntity.ok(new Response(user,200,"User has been registered !"));
                }
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Response(null,301,"An error has occured !"));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new Response(null,301,"Username or email already exists !"));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response(null,301,"A field is empty !"));
    }

    private boolean handleRegistration(User user) throws SQLException {
        InsertValuesStep4<Record, Integer, String, String, String> insert = databaseConnection.getContext()
                .insertInto(table("users"))
                .columns(field("id", Integer.class), field("username", String.class), field("password", String.class), field("mail", String.class))
                .values(null, user.getUsername(), BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()), user.getMail());
        int numberRowsInjected = insert.execute();

        // On récupère le userId créé par la bdd

        Integer userId = DatabaseUtils.getUserId(user,databaseConnection);
        user.setId(userId);

        Boolean emptyCookieInserted = insertEmptyCookie(userId,databaseConnection);

        return numberRowsInjected == 1 && emptyCookieInserted;
    }

    private boolean isUsernameOrEmailAlreadyTook(User user, DatabaseConnector databaseConnection) throws SQLException {
        Boolean usernameExists = DatabaseUtils.checkIfUsernameExists(user,databaseConnection);
        Boolean emailExists = DatabaseUtils.checkIfEmailExists(user,databaseConnection);
        return usernameExists || emailExists;
    }
}
