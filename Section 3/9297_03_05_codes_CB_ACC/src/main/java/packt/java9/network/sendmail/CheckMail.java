package packt.java9.network.sendmail;

import javax.mail.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import static java.lang.System.Logger.Level.INFO;

public class CheckMail {
    private static final System.Logger LOG = System.getLogger(CheckMail.class.getName());

    public static void check(String user, String password) {
        try {
            final String host = "pop.gmail.com";
            Properties props = PropertyBuilder.build(
                    "mail.pop3.host", host,
                    "mail.pop3.port", "995",
                    "mail.pop3.starttls.enable", "true");
            Session session = SessionBuilder.build(props, user, password);

            Store store = session.getStore("pop3s");
            store.connect(host, user, password);
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();
            LOG.log(INFO, "Number of messages in folder INBOX: " + messages.length);

            logMessages(messages);
            closeFolderAndStore(emailFolder, store);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void closeFolderAndStore(Folder emailFolder, Store store) throws MessagingException {
        emailFolder.close(false);
        store.close();
    }

    private static void logMessages(Message... messages) throws MessagingException, IOException {
        int i = 1;
        for (final Message message : messages) {
            LOG.log(INFO,
                    "Email Number " + i + "\n"
                            + "Subject: " + message.getSubject() + "\n"
                            + "From: " +
                            Arrays.stream(message.getFrom())
                                    .map(Address::toString)
                                    .collect(Collectors.joining(",")) + "\n"
                            + "Text: " + message.getContent().toString()
                            + "---------------------------------" + "\n");
            i++;
        }
    }
}
