package com.jackalhan.ualr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * Created by txcakaloglu on 5/12/16.
 */
@Service
@PropertySource("classpath:ftp.properties")
public class FTPConfiguration {

    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;
    @Value("${ftp.remoteDirectory}")
    private String remoteDirectory;
    @Value("${ftp.port}")
    private int port;
    @Value("${ftp.file.processed.path}")
    private String fileProcessedPath;
    @Value("${ftp.file.temp.path}")
    private String fileTempPath;
    @Value("${ftp.file.error.path}")
    private String fileErrorPath;
    @Value("${ftp.file.ignored.path}")
    private String fileIgnoredPath;


    public String getFileProcessedPath() {
        return fileProcessedPath;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRemoteDirectory() {
        return remoteDirectory;
    }

    public int getPort() {
        return port;
    }

    public String getFileTempPath() {
        return fileTempPath;
    }

    public String getFileErrorPath() {
        return fileErrorPath;
    }

    public String getFileIgnoredPath() {
        return fileIgnoredPath;
    }

    @Override
    public String toString() {
        return "FTPConfiguration{" +
                "host='" + host + '\'' +
                ", username='" + username.substring(0,3) + "********" + '\'' +
                ", password='" + "*********" + '\'' +
                ", remoteDirectory='" + remoteDirectory + '\'' +
                ", port=" + port +
                ", fileProcessedPath=" + fileProcessedPath +
                ", fileTempPath=" + fileTempPath +
                ", fileErrorPath=" + fileErrorPath +
                ", fileIgnoredPath=" + fileIgnoredPath+
                '}';
    }
}
