package org.example.rest.ApiControllers;

import classes.User;
import database.DatabaseConnector;
import org.apache.logging.log4j.util.Strings;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.mindrot.jbcrypt.BCrypt;
import javax.servlet.http.Cookie;
import java.util.UUID;
import java.sql.SQLException;
import java.util.Objects;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class LoginApi {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) throws SQLException {

        if (Strings.isNotBlank(user.getUsername())
                && Strings.isNotBlank(user.getPassword())) {
            if(compareHashedPassword(user)) {
                Cookie cookie = createCookie(user);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
                String responseBody = "You're logged in !";
                HttpStatus status = HttpStatus.OK;
                ResponseEntity<String> responseWithCookie = new ResponseEntity<>(responseBody, headers, status);
                return responseWithCookie;
            }
            return ResponseEntity.ok("Wrong user or password !");
        }
        return ResponseEntity.ok("Fields empty !");
    }


    private String getHashedPasswordFromUsername(User user) throws SQLException {

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
        if(result.isNotEmpty()){
            return Objects.requireNonNull(result.get(0).get(2)).toString();
        }
        return "";
    }

    private boolean compareHashedPassword(User user) throws SQLException {
        String hashedPassword = getHashedPasswordFromUsername(user);
            if (hashedPassword.isEmpty()) {
                return false;
            }
        return BCrypt.checkpw(user.getPassword(),hashedPassword);
    }

    private Cookie createCookie(User user) throws SQLException {
        DatabaseConnector databaseConnection = new DatabaseConnector();
        // Récupération de l'id du client
        SelectQuery<Record> query = databaseConnection.getContext().selectQuery();
        Table<Record> usersTable = table("users");
        Field<String> usernameField = field("username", String.class);
        query.addSelect(usersTable.fields());
        query.addFrom(usersTable);
        query.addConditions(usernameField.eq(user.getUsername()));
        // Execute the query
        Result<Record> result = query.fetch();
        String cookieName = "auth";
        String cookieValue = "";
        Cookie cookie = new Cookie(cookieName, cookieValue);

        if(result.isNotEmpty()) {
            int user_id = (Integer) result.get(0).get(0);
            // Création du cookie
            cookieValue = UUID.randomUUID().toString();
            // Stockage du cookie en base
            InsertValuesStep2<Record, Integer, String> insert = databaseConnection.getContext()
                    .insertInto(table("users_configuration"))
                    .columns(field("user_id", Integer.class), field("cookie", String.class))
                    .values(user_id, cookieValue);
            int numberRowsInjected = insert.execute();
            if (numberRowsInjected == 1) {
                cookie.setValue(cookieValue);
            }
            databaseConnection.getDataSource().getConnection().close();
        }
        return cookie;
    }
}
