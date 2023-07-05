package org.example.rest.ApiControllers;

import classes.User;
import org.example.rest.database.DatabaseConnector;
import org.example.rest.mail.EmailVerification;
import org.jooq.*;
import org.jooq.Record;

import java.util.Random;

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
        InsertValuesStep3<Record, Integer, String, String> insertCookie = databaseConnection.getContext()
                .insertInto(table("users_configuration"))
                .columns(field("user_id", Integer.class), field("cookie", String.class),field("mail_verification", String.class))
                .values(userId, "","");
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

    public static boolean checkIfEmailExists(User user, DatabaseConnector databaseConnection) {
        SelectQuery<Record> query = databaseConnection.getContext().selectQuery();

        Table<Record> usersTable = table("users");
        Field<String> usernameField = field("mail", String.class);
        query.addSelect(usersTable.fields());
        query.addFrom(usersTable);
        query.addConditions(usernameField.eq(user.getMail()));
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

    public static String getUserMail(User user, DatabaseConnector databaseConnection) {
        SelectQuery<Record> query = databaseConnection.getContext().selectQuery();
        Table<Record> usersTable = table("users");
        Field<String> usernameField = field("mail", String.class);
        query.addSelect(usersTable.fields());
        query.addFrom(usersTable);
        query.addConditions(usernameField.eq(user.getUsername()));
        // Execute the query
        Result<Record> result = query.fetch();
        if (result.isNotEmpty()) {
            return (String) result.get(0).get(0);
        }
        return null;
    }

    public static String UpdateVerificationCode(int userId, DatabaseConnector databaseConnection, String mail) {
        Random random = new Random();
        int code = random.nextInt(1000000); // Génère un nombre aléatoire entre 0 et 999999
        String formattedCode = String.format("%06d", code);
        UpdateConditionStep<Record> query = databaseConnection.getContext().update(table("users_configuration"))
                .set(field("mail_verification"), formattedCode)
                .where(field("user_id").eq(userId));
        if(query.execute() == 1) {
            EmailVerification emailVerification = new EmailVerification();
            emailVerification.SendVerificationMail(mail, formattedCode);
            return formattedCode;
        }
        return null;
    }
}
