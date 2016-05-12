package com.jackalhan.ualr.service.rest;

import com.jackalhan.ualr.config.FTPConfiguration;
import com.jackalhan.ualr.domain.FTPConnection;
import com.jackalhan.ualr.service.utils.StringUtilService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by txcakaloglu on 5/12/16.
 */
@Service
public class FTPService {

    private final Logger log = LoggerFactory.getLogger(StringUtilService.class);

    @Autowired
    private FTPConfiguration ftpConfiguration;

    private FTPConnection connect() {

        Session session = null;
        Channel channel = null;
        try {

            String sessionChannel = ((ftpConfiguration.getPort() < 22) ? "ftp" : "sftp");
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            JSch ssh = new JSch();
            session = ssh.getSession(ftpConfiguration.getUsername(), ftpConfiguration.getHost(), ftpConfiguration.getPort());
            session.setConfig(config);
            session.setPassword(ftpConfiguration.getPassword());
            session.connect();
            channel = session.openChannel(sessionChannel);
            channel.connect();
            log.info("Application is connected to " + ftpConfiguration.toString());
        } catch (Exception ex) {
            log.error(ex.toString());
        }

        return new FTPConnection(session, channel);
    }

    private void disconnect(FTPConnection connection)
    {
        try
        {
            if (connection.getChannel().isConnected())
            {
                connection.getChannel().disconnect();
            }
            if (connection.getSession().isConnected())
            {
                connection.getSession().disconnect();
            }
            log.info("Application is disconnected from " + ftpConfiguration.toString());

        }
        catch (Exception ex)
        {
            log.error(ex.toString());
        }
    }

    public


    ChannelSftp sftp = (ChannelSftp) channel;
    sftp.cd(directory);
    //Vector files = sftp.ls("*");
    Vector<ChannelSftp.LsEntry> list = sftp.ls("*");
       /* for(ChannelSftp.LsEntry entry : list) {
            channelSftp.get(entry.getFileName(), destinationPath + entry.getFileName());
        }
        */

    System.out.printf("Found %d files in dir %s%n", list.size(), directory);

    for (ChannelSftp.LsEntry file : list) {
        if (file.getAttrs().isDir()) {
            continue;
        }
        System.out.printf("Reading file : %s%n", file.getFilename());
        BufferedReader bis = new BufferedReader(new InputStreamReader(sftp.get(file.getFilename())));
        String line = null;
        while ((line = bis.readLine()) != null) {
            System.out.println(line);
        }
        bis.close();
    }

    channel.disconnect();
    session.disconnect();

    public void getFiles()
}

