package org.example.rest.ApiControllers;

import classes.User;
import database.DatabaseConnector;
import org.apache.logging.log4j.util.Strings;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/test")
public class LoginApi {

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) throws SQLException {

        if (Strings.isNotBlank(user.getUsername())
                && Strings.isNotBlank(user.getPassword())) {
            verifyUsernameAndPassword(user);
        }
        return ResponseEntity.ok("Fields empty !");
    }


    private boolean verifyUsernameAndPassword(User user) throws SQLException {

        DatabaseConnector databaseConnection = new DatabaseConnector();
        SelectQuery<Record> query = databaseConnection.getContext().selectQuery();
        Table<Record> usersTable = table("users");
        Field<String> usernameField = field("username", String.class);
        Field<String> passwordField = field("password", String.class);
        query.addSelect(usersTable.fields());
        query.addFrom(usersTable);
        query.addConditions(usernameField.eq(user.getUsername()));
        query.addConditions(passwordField.eq(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt())));
        // Execute the query
        Result<Record> result = query.fetch();
        databaseConnection.getDataSource().getConnection().close();
        return !result.isEmpty();
    }
}
