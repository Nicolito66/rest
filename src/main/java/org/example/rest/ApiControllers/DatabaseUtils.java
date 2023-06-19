package org.example.rest.ApiControllers;

import classes.User;
import database.DatabaseConnector;
import org.jooq.*;
import org.jooq.Record;

import javax.servlet.http.Cookie;
import javax.xml.crypto.Data;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class DatabaseUtils {

    public DatabaseUtils() {
    }

    public static Integer getUserId(User user, DatabaseConnector databaseConnection) {
        SelectQuery<Record> query = databaseConnection.getContext().selectQuery();
        Table<Record> usersTable = table("users");
        Field<String> usernameField = field("username", String.class);
        query.addSelect(usersTable.fields());
        query.addFrom(usersTable);
        query.addConditions(usernameField.eq(user.getUsername()));
        // Execute the query
        Result<Record> result = query.fetch();
        if (result.isNotEmpty()) {
            return (Integer) result.get(0).get(0);
        }
        return -1;
    }
    public static boolean insertEmptyCookie(int userId, DatabaseConnector databaseConnection) {
        InsertValuesStep2<Record, Integer, String> insertCookie = databaseConnection.getContext()
                .insertInto(table("users_configuration"))
                .columns(field("user_id", Integer.class), field("cookie", String.class))
                .values(userId, "");
        int numberRowsInjected = insertCookie.execute();

        return numberRowsInjected == 1;
    }

    public static boolean checkIfUsernameExists(User user, DatabaseConnector databaseConnection) {
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

    public static boolean UpdateCookie(int userId, DatabaseConnector databaseConnection, String cookieValue) {
        UpdateConditionStep<Record> query = databaseConnection.getContext().update(table("users_configuration"))
                .set(field("cookie"), cookieValue)
                .where(field("user_id").eq(userId));
        int numberRowsInjected = query.execute();


        return numberRowsInjected == 1;
    }
}
