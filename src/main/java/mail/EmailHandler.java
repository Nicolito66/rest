package mail;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailHandler {

    private static String smtpHost = "smtp.office365.com";
    private String username;
    private String password;
    private String fromAddress;

    private static int smtpPort = 587;

    public EmailHandler(String username, String password) {
        this.username = username;
        // Pour le moment l'adresse d'envoie est l'adresse principale
        this.fromAddress = username;
        this.password = password;
    }

    public void sendEmail(String toAddress, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Création de l'objet Message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            message.setSubject(subject);
            message.setText(body);

            // Envoi de l'e-mail
            Transport.send(message);

            System.out.println("E-mail envoyé avec succès !");
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
        }

    }
}
