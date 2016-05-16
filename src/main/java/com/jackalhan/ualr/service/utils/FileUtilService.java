package com.jackalhan.ualr.service.utils;


import com.jackalhan.ualr.domain.model.WorkloadReport;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by jackalhan on 4/25/16.
 */
public class FileUtilService {
    private final Logger log = LoggerFactory.getLogger(StringUtilService.class);


    private static FileUtilService singleton;
    private File file;

    public FileUtilService() {
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

    public boolean moveTo(String sourcePath, String destinationPath, String fileName)
    {
        boolean result = true;
        try
        {
            File afile =new File(sourcePath + fileName);
            createDirectory(destinationPath);
            if(afile.renameTo(new File(destinationPath + afile.getName()))){
                log.info( fileName + " is  moved from " + sourcePath + " to " + destinationPath);
            }else{
                result = false;
                log.error(sourcePath + fileName + " is failed to move to " + destinationPath);
            }
        }
        catch (Exception ex)
        {
            result = false;
            log.error(ex.toString());
        }
        return result;

    }

    public File convertToFile(String content, String destinationPath, String fileName)
    {
        try {
            createDirectory(destinationPath);
            return Files.write(Paths.get(destinationPath + fileName), content.getBytes()).toFile();

        } catch (IOException e) {

            log.error(e.toString());
        }
        return null;
    }

    public File getFileFromResources(String path) {
        File file = null;
        try {
            Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(
                    ClasspathHelper.forPackage(path)).setScanners(new ResourcesScanner()));

            Set<String> properties = reflections.getResources(Pattern.compile(path));
            ClassLoader classLoader = getClass().getClassLoader();
            file = new File(classLoader.getResource(properties.toArray()[0].toString()).getFile());
            log.info("File from this " + path + " is delivered to requested method!");

        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return file;
    }


    public File getFile(String path) {
        File file = null;
        try {
            file = new File(path);
            log.info("File from this " + path + " is delivered to requested method!");
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return file;
    }

    private BufferedReader getFileContent() {
        return getFileContent(this.file);
    }

    public BufferedReader getFileContent(File file) {
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(file));
            log.info(file + "'s content is delivered to requested method!");
        } catch (FileNotFoundException e) {
            log.error(e.toString());
        } catch (IOException e) {
            log.error(e.toString());
        }
        return br;
    }

    public List<WorkloadReport> listFolders(String directoryName){
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        List<WorkloadReport> files = new ArrayList<WorkloadReport>();
        for (File file : fList){
            //if (file.isDirectory()){
                WorkloadReport workloadReport = new WorkloadReport();
                workloadReport.setFilePath(directoryName + file.getName());
                files.add(workloadReport);
            //}
        }
        return files;
    }


    private File getFile() {
        return file;
    }

    private void setFile(File file) {
        this.file = file;
    }



}
