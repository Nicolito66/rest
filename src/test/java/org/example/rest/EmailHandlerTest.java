package org.example.rest;

import org.example.rest.mail.EmailHandler;
import org.example.rest.mail.EmailVerification;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootTest
public class EmailHandlerTest {

    private String username;
    private String password;

    @Test
    public void sendEmail() {
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream("/home/nicolas/project/rest/config.properties")) {
            properties.load(input);

            // Lecture des mots de passe pour les utilisateurs
            username = properties.getProperty("smtp.user");
            password = properties.getProperty("smtp.password");

            EmailHandler emailHandler = new EmailHandler(username,password);
            emailHandler.sendEmail(username,"Hello Test","Ceci est le body du mail 2");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void sendVerificationEmail() {
        EmailVerification emailVerification = new EmailVerification();
        emailVerification.SendVerificationMail(username,"999999");

    }
}
