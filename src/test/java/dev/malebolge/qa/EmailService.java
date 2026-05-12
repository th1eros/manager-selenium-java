package dev.malebolge.qa;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {
    public static void enviarRelatorio(String mensagemCorpo) {
        final String username = System.getenv("EMAIL_USER");
        final String password = System.getenv("EMAIL_PASS");
        final String destinatarios = "driveunivesp@gmail.com, alexandrebessa87@gmail.com";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatarios));
            message.setSubject("QA-Malebolge: Relatório Projeto Integrador 1 - 2026");
            message.setText(mensagemCorpo);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}