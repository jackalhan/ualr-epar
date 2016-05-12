package com.jackalhan.ualr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by txcakaloglu on 5/12/16.
 */

@PropertySource("classpath:ftp.properties")
public class FTPConfiguration {

    @Value("${host}")
    private String host;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${remoteDirectory}")
    private String remoteDirectory;
    @Value("${port}")
    private int port;

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
}
