package com.jackalhan.ualr.service.utils;


import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;

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

    public File getTestFile (String fileNamePattern)
    {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
                ClasspathHelper.forPackage("testdata")).setScanners(new ResourcesScanner()));

        Set<String> properties = reflections.getResources(Pattern.compile(fileNamePattern + ".*\\.csv"));
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(properties.toArray()[0].toString()).getFile());
        return file;
    }

}
