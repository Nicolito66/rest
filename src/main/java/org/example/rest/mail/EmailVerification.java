package org.example.rest.mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailVerification {

    public EmailVerification() {
    }

    private String buildVerificationMailBody(String secretCode) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <title>Code de verification</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      font-family: Arial, sans-serif;\n" +
                "      background-color: #f5f5f5;\n" +
                "      margin: 0;\n" +
                "      padding: 20px;\n" +
                "    }\n" +
                "\n" +
                "    .container {\n" +
                "      max-width: 600px;\n" +
                "      margin: 0 auto;\n" +
                "      background-color: #1f2937;\n" +
                "      color: #ffffff;\n" +
                "      padding: 20px;\n" +
                "      border-radius: 4px;\n" +
                "      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
                "    }\n" +
                "\n" +
                "    h2 {\n" +
                "      color: #ffffff;\n" +
                "    }\n" +
                "\n" +
                "    .verification-code {\n" +
                "      background-color: #718096;\n" +
                "      color: #ffffff;\n" +
                "      padding: 10px;\n" +
                "      font-size: 24px;\n" +
                "      display: inline-block;\n" +
                "      margin-top: 10px;\n" +
                "      margin-bottom: 20px;\n" +
                "      border-radius: 4px;\n" +
                "    }\n" +
                "\n" +
                "    p {\n" +
                "      color: #d2d6dc;\n" +
                "    }\n" +
                "\n" +
                "    .signature {\n" +
                "      margin-top: 40px;\n" +
                "      color: #a0aec0;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <h2>Code de verification</h2>\n" +
                "    <p>Merci de votre inscription. Voici votre code de verification :</p>\n" +
                "    <div class=\"verification-code\">"+secretCode+"</div>\n" +
                "    <p>Copiez ce code et utilisez-le pour vérifier votre compte.</p>\n" +
                "    <p class=\"signature\">Cordialement,<br> Votre equipe de support</p>\n" +
                "  </div>\n" +
                "</body>\n" +
                "</html>\n";
    }

    public void SendVerificationMail(String mailToAdress, String secretCode) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("/home/nicolas/project/rest/src/main/java/org/example/rest/ApiControllers/config.properties")) {
            properties.load(input);
            // Lecture des mots de passe pour les utilisateurs
            String username = properties.getProperty("smtp.user");
            String password = properties.getProperty("smtp.password");
            EmailHandler emailHandler = new EmailHandler(username,password);
            emailHandler.sendEmail(mailToAdress,"Code de vérification",buildVerificationMailBody(secretCode));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
