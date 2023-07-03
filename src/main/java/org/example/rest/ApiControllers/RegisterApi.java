package org.example.rest.ApiControllers;

import classes.User;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.DatabaseConnector;
import org.apache.logging.log4j.util.Strings;
import org.jooq.*;
import org.jooq.Record;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.sql.SQLException;

import static org.example.rest.ApiControllers.DatabaseUtils.insertEmptyCookie;
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
    public ResponseEntity<User> register(@RequestBody User user) throws SQLException {
        DatabaseConnector databaseConnection = new DatabaseConnector();
        if (Strings.isNotBlank(user.getUsername())
                && Strings.isNotBlank(user.getPassword())
                && Strings.isNotBlank(user.getMail())) {
            if (!isUsernameOrEmailAlreadyTook(user, databaseConnection)) {
                if (handleRegistration(user, databaseConnection)) {
                        DatabaseUtils.UpdateVerificationCode(user.getId(), databaseConnection,user.getMail());
                    return ResponseEntity.ok(user);
                }
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();
    }

    private boolean handleRegistration(User user,DatabaseConnector databaseConnection) throws SQLException {
        InsertValuesStep4<Record, Integer, String, String, String> insert = databaseConnection.getContext()
                .insertInto(table("users"))
                .columns(field("id", Integer.class), field("username", String.class), field("password", String.class), field("mail", String.class))
                .values(null, user.getUsername(), BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()), user.getMail());
        int numberRowsInjected = insert.execute();
        databaseConnection.getDataSource().getConnection().close();

        // On récupère le userId créé par la bdd

        Integer userId = DatabaseUtils.getUserId(user,databaseConnection);
        user.setId(userId);

        Boolean emptyCookieInserted = insertEmptyCookie(userId,databaseConnection);

        return numberRowsInjected == 1 && emptyCookieInserted;
    }

    private boolean isUsernameOrEmailAlreadyTook(User user, DatabaseConnector databaseConnection) throws SQLException {
        Boolean usernameExists = DatabaseUtils.checkIfUsernameExists(user,databaseConnection);
        Boolean emailExists = DatabaseUtils.checkIfEmailExists(user,databaseConnection);
        databaseConnection.getDataSource().getConnection().close();
        return usernameExists || emailExists;
    }
}
