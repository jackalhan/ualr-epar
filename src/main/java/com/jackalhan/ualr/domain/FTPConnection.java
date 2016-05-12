package com.jackalhan.ualr.domain;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

/**
 * Created by txcakaloglu on 5/12/16.
 */
public class FTPConnection {

    private Session session;
    private Channel channel;

    public FTPConnection(Session session, Channel channel) {
        this.session = session;
        this.channel = channel;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "FTPConnection{" +
                "session=" + session +
                ", channel=" + channel +
                '}';
    }
}
