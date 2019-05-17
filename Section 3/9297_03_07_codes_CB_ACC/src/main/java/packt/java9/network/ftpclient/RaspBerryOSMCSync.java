package packt.java9.network.ftpclient;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static packt.java9.network.ftpclient.Parameters.OSMC_SERVER;
import static packt.java9.network.ftpclient.Parameters.password;
import static packt.java9.network.ftpclient.Parameters.user;

public class RaspBerryOSMCSync {
    private static final System.Logger LOG = System.getLogger(RaspBerryOSMCSync.class.getName());

    public static void main(String[] args) {
        new RaspBerryOSMCSync().sync();
    }

    public void sync() {
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);
        try {
            ftp.connect(OSMC_SERVER);
            ftp.login(user, password);
            final int replyCode = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                LOG.log(System.Logger.Level.ERROR, "Not connected");
                ftp.disconnect();
                return;
            }
            FTPFile[] files = ftp.listFiles();
            for( FTPFile file : files){
                System.out.println(String.format("%20s %d",file.getName(),file.getSize()));
            }
            ftp.changeWorkingDirectory("Movies");
            listFilesInCwd(ftp);
            InputStream sample = new FileInputStream("sample.txt");
            ftp.storeFile("sample.txt",sample);
            listFilesInCwd(ftp);
        } catch (IOException e) {
            LOG.log(System.Logger.Level.ERROR, e.toString());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    LOG.log(System.Logger.Level.ERROR, "IOException while disconnecting");
                    LOG.log(System.Logger.Level.ERROR, e.toString());
                }
            }
        }
    }

    private void listFilesInCwd(FTPClient ftp) throws IOException {
        FTPFile[] files;
        System.out.println("---------------------------------------------");
        files = ftp.listFiles();
        for( FTPFile file : files){
            System.out.println(String.format("%20s %d",file.getName(),file.getSize()));
        }
    }
}

