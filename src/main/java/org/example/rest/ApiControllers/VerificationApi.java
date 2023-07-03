package org.example.rest.ApiControllers;


import classes.User;
import classes.VerificationInfos;
import database.DatabaseConnector;
import org.apache.logging.log4j.util.Strings;
import org.jooq.*;
import org.jooq.Record;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Objects;

import static org.example.rest.ApiControllers.DatabaseUtils.insertEmptyCookie;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class VerificationApi {

    @PutMapping("/verification")
    public ResponseEntity<String> verification(@RequestBody VerificationInfos verificationInfos) throws SQLException {
        DatabaseConnector databaseConnection = new DatabaseConnector();
        if (Strings.isNotBlank(verificationInfos.getCode()) && Strings.isNotBlank(verificationInfos.getId())) {
            if(handleVerification(verificationInfos,databaseConnection)){
                return ResponseEntity.ok("User has been registered");
            }
        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Field Empty !");
    }

    private boolean handleVerification(VerificationInfos verificationInfos, DatabaseConnector databaseConnection) throws SQLException {

        SelectQuery<Record> query = databaseConnection.getContext().selectQuery();
        Table<Record> usersTable = table("users_configuration");
        Field<String> idField = field("user_id", String.class);
        query.addSelect(usersTable.fields());
        query.addFrom(usersTable);
        query.addConditions(idField.eq(verificationInfos.getId()));
        // Execute the query
        Result<Record> result = query.fetch();
        if(Objects.equals(result.get(0).get(2), verificationInfos.getCode())) {
            UpdateConditionStep<Record> update = databaseConnection.getContext().update(table("users"))
                    .set(field("verified"), 1)
                    .where(field("id").eq(verificationInfos.getId()));
            int numberRowsInjected = update.execute();
            return numberRowsInjected == 1;
        }
        System.out.println(query);

        return false;
    }


}
