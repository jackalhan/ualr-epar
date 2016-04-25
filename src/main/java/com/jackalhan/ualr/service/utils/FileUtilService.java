package com.jackalhan.ualr.service.utils;

import com.jackalhan.ualr.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by jackalhan on 4/25/16.
 */
public class FileUtilService {
    private final Logger log = LoggerFactory.getLogger(StringUtilService.class);


    private static FileUtilService singleton;

    private FileUtilService() {
    }

    public static synchronized FileUtilService getInstance() {
        if (singleton == null)
            singleton = new FileUtilService();
        return singleton;
    }

    public void createDirectory(String folderPath) {

        try {
            File files = new File(folderPath);
            if (!files.exists()) {
                if (files.mkdirs()) {
                    log.info(folderPath + " directories are created!");
                } else {
                    log.info("Failed to create multiple directories!");
                }
            }
        } catch (Exception ex) {
            log.warn(ex.toString());
        }

    }


}
