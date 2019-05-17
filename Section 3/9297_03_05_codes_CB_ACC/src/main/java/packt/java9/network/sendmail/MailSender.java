package packt.java9.network.sendmail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {
    public static void sendMail(String user, String to, String password) {
        final Properties props = PropertyBuilder.build(
                "mail.smtp.host", "smtp.gmail.com",
                "mail.smtp.socketFactory.port", "465",
                "mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory",
                "mail.smtp.auth", "true",
                "mail.smtp.port", "465");
        Session session = SessionBuilder.build(props, user, password);
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("hello me");
            message.setText("This is the message in the mail");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
