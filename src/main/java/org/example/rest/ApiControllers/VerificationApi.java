package org.example.rest.ApiControllers;

import classes.Response;
import classes.VerificationInfos;
import database.DatabaseConnector;
import org.apache.logging.log4j.util.Strings;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class VerificationApi {

    @PutMapping("/verification")
    public ResponseEntity<Response> verification(@RequestBody VerificationInfos verificationInfos) {
        DatabaseConnector databaseConnection = new DatabaseConnector();
        if (Strings.isNotBlank(verificationInfos.getCode()) && Strings.isNotBlank(verificationInfos.getId())) {
            if(handleVerification(verificationInfos,databaseConnection)){
                return ResponseEntity.ok(new Response(null,200,"User has been verified"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new Response(null,200,"Wrong code !"));
    }

    private boolean handleVerification(VerificationInfos verificationInfos, DatabaseConnector databaseConnection) {
        SelectQuery<Record> query = databaseConnection.getContext().selectQuery();
        Table<Record> usersTable = table("users_configuration");
        Field<String> idField = field("user_id", String.class);
        query.addSelect(usersTable.fields());
        query.addFrom(usersTable);
        query.addConditions(idField.eq(verificationInfos.getId()));
        // Execute the query
        Result<Record> result = query.fetch();
        if(Objects.equals(result.get(0).get(2), verificationInfos.getCode())) {
            return setVerifiedAccount(verificationInfos.getId(),databaseConnection);
        }
        return false;
    }

    private boolean setVerifiedAccount(String userId, DatabaseConnector databaseConnection) {
        UpdateConditionStep<Record> update = databaseConnection.getContext().update(table("users"))
                .set(field("verified"), 1)
                .where(field("id").eq(userId));
        int numberRowsInjected = update.execute();
        return numberRowsInjected == 1;
    }
}
