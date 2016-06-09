package com.jackalhan.ualr.service.rest;

import com.jackalhan.ualr.config.FTPConfiguration;
import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.domain.FTPConnection;
import com.jackalhan.ualr.domain.FTPFile;
import com.jackalhan.ualr.service.utils.FileUtilService;
import com.jackalhan.ualr.service.utils.StringUtilService;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by txcakaloglu on 5/12/16.
 */
@Service
public class FTPService {

    private final Logger log = LoggerFactory.getLogger(FTPService.class);

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

    private void disconnect(FTPConnection connection) {
        try {
            if (connection.getChannel().isConnected()) {
                connection.getChannel().disconnect();
            }
            if (connection.getSession().isConnected()) {
                connection.getSession().disconnect();
            }
            log.info("Application is disconnected from " + ftpConfiguration.toString());

        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

    public List<FTPFile> listFiles(String filePattern) {
        List<FTPFile> files = null;
        FTPConnection connection = null;
        try {

            connection = connect();
            ChannelSftp channel = (ChannelSftp) connection.getChannel();

            // channel.cd(ftpConfiguration.getRemoteDirectory()); // default value enter the welcome folder
            Vector<ChannelSftp.LsEntry> lsEntryVector = channel.ls(filePattern); //Vector is a technically list. Vector implementation of list.
            files = new ArrayList<FTPFile>();
            for (ChannelSftp.LsEntry entry : lsEntryVector) {
                FTPFile ftpFile = new FTPFile();
                ftpFile.setFileName(entry.getFilename());
                ftpFile.setFileUploadedDate(StringUtilService.getInstance().dateToTargetFormat(entry.getAttrs().getMtimeString(), GenericConstant.SIMPLE_DATE_TIME_DEFAULT_ALL_DATE_FORMAT, GenericConstant.SIMPLE_DATE_TIME_DATE_AND_TIME));
                ftpFile.setFileUploadedDateAsInt(entry.getAttrs().getMTime());
                files.add(ftpFile);
                log.info(entry.getFilename() + " is found");
            }

        } catch (SftpException e) {
            log.error(e.toString());
        } finally {
            disconnect(connection);
        }
        return files;

    }

    private void createFolder(ChannelSftp channelSftp, String path) throws SftpException {

        String[] folders = path.split("/");
        for (String folder : folders) {
            if (folder.length() > 0) {
                try {
                    channelSftp.cd(folder);
                } catch (SftpException e) {
                    channelSftp.mkdir(folder);
                    channelSftp.cd(folder);
                }
            }
        }
    }

    public List<String> downloadAndGetExactFileNames(String localDownloadPath, String filePattern)
    {
        List<String> fileNames = null;
        FTPConnection connection = null;
        try {

            connection = connect();
            ChannelSftp channel = (ChannelSftp) connection.getChannel();
            Vector<ChannelSftp.LsEntry> lsEntryVector = channel.ls(filePattern); //Vector is a technically list. Vector implementation of list.
            FileUtilService.getInstance().createDirectory(localDownloadPath);
            fileNames = new ArrayList<String>();
            for (ChannelSftp.LsEntry entry : lsEntryVector) {
                channel.get(entry.getFilename(), localDownloadPath + entry.getFilename());
                log.info(entry.getFilename() + " is downloaded to " + localDownloadPath);
                moveTo(ftpConfiguration.getRemoteDirectory(), ftpConfiguration.getFileTempPath(), entry.getFilename());
                fileNames.add(entry.getFilename());
            }

        } catch (SftpException e) {
            log.error(e.toString());
        } finally {
            disconnect(connection);
        }
        return fileNames;
    }

    public boolean moveTo(String sourcePath, String destinationPath, String fileName) {
        boolean result = true;
        FTPConnection connection = null;
        ChannelSftp channel = null;
        try {
            connection = connect();
            channel = (ChannelSftp) connection.getChannel();
            createFolder(channel, destinationPath);
            channel.rename(channel.getHome() + "/" + sourcePath + fileName, channel.getHome() + destinationPath + fileName);
            log.info(fileName + " is successfuly moved from " + channel.getHome() + " to " + channel.getHome() + destinationPath);
        } catch (Exception ex) {
            try
            {
                channel.rm(channel.getHome() + destinationPath + fileName);
                channel.rename(channel.getHome() + "/" + sourcePath + fileName, channel.getHome() + destinationPath + fileName);
                log.info(fileName + " is successfuly deleted and moved from " + channel.getHome() + " to " + channel.getHome() + destinationPath);
            }
            catch (Exception e) {
                result = false;
                log.error(ex.toString());
            }
        }
        finally {
            disconnect(connection);
        }

        return result;
    }
/*

    ChannelSftp sftp = (ChannelSftp) channel;
    sftp.cd(directory);
    //Vector files = sftp.ls("*");
    Vector<ChannelSftp.LsEntry> list = sftp.ls("*");
       *//* for(ChannelSftp.LsEntry entry : list) {
            channelSftp.get(entry.getFileName(), destinationPath + entry.getFileName());
        }
        *//*

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

    public void getFiles()*/
}

