package mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailVerification {

    public EmailVerification() {
    }

    public void SendVerificationMail(String mailToAdress, String secretCode) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("/home/nicolas/project/rest/src/main/java/org/example/rest/ApiControllers/config.properties")) {
            properties.load(input);

            // Lecture des mots de passe pour les utilisateurs
            String username = properties.getProperty("smtp.user");
            String password = properties.getProperty("smtp.password");

            EmailHandler emailHandler = new EmailHandler(username,password);
            emailHandler.sendEmail(mailToAdress,"Code de vérification","Le code de vérification est " + secretCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
