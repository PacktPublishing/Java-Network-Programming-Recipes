package packt.java9.network.sshclient;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;

import static com.jcraft.jsch.ChannelSftp.OVERWRITE;
import static java.lang.System.Logger.Level.INFO;
import static packt.java9.network.sshclient.Parameters.*;

public class RaspBerryOSMCSync {
    private static final System.Logger LOG = System.getLogger(RaspBerryOSMCSync.class.getName());

    public static void main(String[] args) throws IOException, JSchException, SftpException {
        new RaspBerryOSMCSync().sync();
    }

    public void sync() throws JSchException, IOException, SftpException {
        JSch jsch = new JSch();

        final Session session = jsch.getSession(user, OSMC_SERVER);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        ChannelExec command = (ChannelExec) session.openChannel("exec");
        command.setCommand("cd Movies; ls -l");
        StringBuilder out = fetchOutput(command);
        LOG.log(INFO, out.toString());
        command.disconnect();

        final ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
        sftp.connect();
        sftp.put("sample.txt", "Movies/sample_ssh.txt", OVERWRITE);
        sftp.disconnect();

        command = (ChannelExec) session.openChannel("exec");
        command.setCommand("cd Movies; ls -l");
        out = fetchOutput(command);
        LOG.log(INFO, out.toString());
        command.disconnect();

        session.disconnect();
    }

    private StringBuilder fetchOutput(ChannelExec channel) throws IOException, JSchException {
        InputStream commandOutput = channel.getInputStream();
        channel.connect();
        int readByte = commandOutput.read();
        StringBuilder out = new StringBuilder();
        while (readByte != -1) {
            out.append((char) readByte);
            readByte = commandOutput.read();
        }
        return out;
    }
}

