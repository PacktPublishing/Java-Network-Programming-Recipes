package packt.java9.network.sendmail;

import static java.lang.System.Logger.Level.INFO;

public class Main {
    private static final System.Logger LOG = System.getLogger(Main.class.getName());

    public static void main(String[] args) {
        final String from = "nqcgmgjhw8dssqtt@gmail.com";
        final String to = from;
        final String password = "MjMrp7WTsnnrs5V3JjCFKo3xf25WsDSM";

        MailSender.sendMail(from,to,password);
        LOG.log(INFO, "message sent");
        CheckMail.check(from, password);
    }
}
