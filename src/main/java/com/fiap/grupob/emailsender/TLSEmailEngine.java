package com.fiap.grupob.emailsender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TLSEmailEngine {
    private static String smtp_server = getEnv("SENDERMAIL_SMTP_SERVER");
    private static String smtp_username = getEnv("SENDERMAIL_SMTP_USERNAME");
    private static String smtp_frommail = getEnv("SENDERMAIL_SMTP_FROMMAIL");
    private static String smtp_password = getEnv("SENDERMAIL_SMTP_PASSWORD");
    private static String tls_port = getEnv("SENDERMAIL_TLS_PORT");
    public static void sendByTLS(String toEmail, String subject, String emailBody) {
        final String fromEmail = smtp_frommail; //requires valid gmail id
        final String password = smtp_password; // correct password for gmail id

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp_server); //SMTP Host
        props.put("mail.smtp.port", tls_port); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.ssl.enable", "false"); //enable SSL
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        session.setDebug(false);
        sender(session, toEmail, subject, emailBody, fromEmail, smtp_username);

    }

    private static String getEnv(String varName){
        return System.getenv(varName);
    }

    private static void sender(Session session, String toEmail, String subject, String body, String fromMail, String smtpUsername){
        try
        {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/richtext");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(fromMail, smtpUsername));

            msg.setReplyTo(InternetAddress.parse(fromMail, false));

            msg.setSubject(subject, "UTF-8");

            msg.setContent(body, "text/html");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            Logger.getLogger("EmailSender").log(Level.INFO,"Message is ready");
            Transport.send(msg);

            Logger.getLogger("EmailSender").log(Level.INFO,"EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
