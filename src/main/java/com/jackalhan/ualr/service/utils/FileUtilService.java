package com.jackalhan.ualr.service.utils;


import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by jackalhan on 4/25/16.
 */
public class FileUtilService {
    private final Logger log = LoggerFactory.getLogger(StringUtilService.class);


    private static FileUtilService singleton;
    private File file;

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

    public FileUtilService getFile (String fileNamePattern)
    {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
                ClasspathHelper.forPackage("testdata")).setScanners(new ResourcesScanner()));

        Set<String> properties = reflections.getResources(Pattern.compile(fileNamePattern + ".*\\.csv"));
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(properties.toArray()[0].toString()).getFile());
        this.setFile(file);
        return this;
    }

    public BufferedReader getCSVFileContent()
    {
        return getCSVFileContent(this.file);
    }
    public BufferedReader getCSVFileContent(File file)
    {
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error(e.toString());
        }
        return br;
    }



    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
