package org.example.rest.ApiControllers;

import classes.Response;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class LoginApi {

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody User user) throws SQLException {
        DatabaseConnector databaseConnection = new DatabaseConnector();
        if (Strings.isNotBlank(user.getUsername())
                && Strings.isNotBlank(user.getPassword())) {
            if(compareHashedPassword(user, databaseConnection)) {
                Cookie cookie = createCookie(user, databaseConnection);
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.SET_COOKIE, cookie.getValue());
                //FIXME: Passer le cookie dans le header
                //ResponseEntity<String> responseWithCookie = new ResponseEntity<>(responseBody, headers, status);
                return ResponseEntity.ok(new Response(cookie.getValue(),200,"User has been logged in !"));
            }
            return ResponseEntity.badRequest().body(new Response(null,301,"Wrong username or password !"));
        }
        return ResponseEntity.badRequest().body(new Response(null,301,"A field is empty !"));
    }


    private String getHashedPasswordFromUsername(User user, DatabaseConnector databaseConnection) throws SQLException {

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

    private boolean compareHashedPassword(User user, DatabaseConnector databaseConnection) throws SQLException {
        String hashedPassword = getHashedPasswordFromUsername(user, databaseConnection);
            if (hashedPassword.isEmpty()) {
                return false;
            }
        return BCrypt.checkpw(user.getPassword(),hashedPassword);
    }


    private Cookie createCookie(User user, DatabaseConnector databaseConnection) throws SQLException {
        // Récupération de l'id du client
        SelectQuery<Record> query = databaseConnection.getContext().selectQuery();
         int userId = DatabaseUtils.getUserId(user,databaseConnection);
        String cookieName = "auth";
        String cookieValue = "";
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if(userId >= 0) {
            // Création du cookie
            cookieValue = UUID.randomUUID().toString();
            // Stockage du cookie en base
            boolean cookieUpdated = DatabaseUtils.UpdateCookie(userId,databaseConnection,cookieValue);
            if (cookieUpdated) {
                cookie.setValue(cookieValue);
            }
            databaseConnection.getDataSource().getConnection().close();
        }
        return cookie;
    }
}
