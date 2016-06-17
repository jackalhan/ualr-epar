package com.jackalhan.ualr.service;

import org.slf4j.Logger;

/**
 * Created by jackalhan on 6/16/16.
 */
public abstract class LogService {

    public Logger log;

    public void setLog(Logger log) {
        this.log = log;
    }

    public Logger getLog() {
        return log;
    }

}
