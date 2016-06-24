package com.jackalhan.ualr.service.batch;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.constant.SchedulingConstant;
import com.jackalhan.ualr.domain.*;
import com.jackalhan.ualr.service.rest.FTPService;
import com.jackalhan.ualr.service.rest.MailService;
import com.jackalhan.ualr.service.utils.FileUtilService;
import com.jackalhan.ualr.service.utils.StringUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by txcakaloglu on 6/15/16.
 */
//@Component
//@Service
public class EmployeeSyncronizationService extends BatchService {


    //private String fileName;
    //private String NEW_MAIL_SUBJECT;

    @Override
    public void initialize() {
        setLog(LoggerFactory.getLogger(EmployeeSyncronizationService.class));
        setMailSubject("Employee Syncronization Service ");
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    //  @Scheduled(fixedDelay = SchedulingConstant.WORKLOAD_REPORT_SERVICE_EXECUTE_FIXED_DELAY)
/*    public void executeService() {

        List<FTPFile> ftpFiles = ftpService.listFiles(getPrefixNameOfFTPFile() + "*.txt");
        List<File> fileList = getDownloadedFiles(ftpFiles);
        List<RawEmployeeWithValidationResult> rawEmployeeWithValidationResults = processAndValidateFiles(fileList);
        if (rawEmployeeWithValidationResults.size() == 0) {
            mailService.sendNewsletterMail(getMailSubject(), "No file found to be executed", "There is no valid file found based on expected pattern " + getPrefixNameOfFTPFile() + " in FTP server");
        } else {
            result = generateExcelContent(simplifiedWorkloadList, importedFileDate, importedFileTerm);
            if (result) {
                mailService.sendNewsletterMail(getMailSubject(), "It was completed ", "Excel files are generated based on following file " + file + ". You can view all generated files by clicking following link......");

            } else {
                mailService.sendNewsletterMail(NEW_MAIL_SUBJECT, "Problem occured during excel generating", "Excel generating based on following file " + file + " is failed. ");
            }


        }
    }

    public void saveEmployee(List<RawEmployeeWithValidationResult> rawEmployeeWithValidationResults)
    {
        for ()
    }*/

    public List<RawEmployeeWithValidationResult> processAndValidateFiles(List<File> fileList) {
        List<RawEmployeeWithValidationResult> rawEmployeeWithValidationResults = new ArrayList<RawEmployeeWithValidationResult>();
        RawEmployeeWithValidationResult rawEmployeeWithValidationResult;
        boolean result;
        for (File file : fileList) {
            BufferedReader bufferedReader = FileUtilService.getInstance().getFileContent(file);
            rawEmployeeWithValidationResult = parseContent(bufferedReader);
            if (!rawEmployeeWithValidationResult.isHasInvalidatedData()) {
                rawEmployeeWithValidationResults.add(parseContent(bufferedReader));
                result = FileUtilService.getInstance().moveTo(GenericConstant.EMPLOYEE_FILES_RAWTXT_TEMP_PATH, GenericConstant.EMPLOYEE_FILES_RAWTXT_PROCESSED_PATH, file.getName());
                if (result) {
                    result = ftpService.moveTo(ftpConfiguration.getFileTempPath(), ftpConfiguration.getFileProcessedPath(), file.getName());
                    if (!result) {
                        mailService.sendNewsletterMail(getMailSubject(), "Problem occured during file ftp moving", "Problem occured during file moving in ftp. " + file + " was moving from " + ftpConfiguration.getFileTempPath() + " to " + ftpConfiguration.getFileProcessedPath());
                    }

                } else {
                    mailService.sendNewsletterMail(getMailSubject(), "Problem occured during file moving", "Problem occured during file moving. " + file + " was moving from " + GenericConstant.EMPLOYEE_FILES_RAWTXT_TEMP_PATH + " to " + GenericConstant.EMPLOYEE_FILES_RAWTXT_PROCESSED_PATH);

                }
            } else {
                mailService.sendNewsletterMail(getMailSubject(), "Problem occured during data parsing", rawEmployeeWithValidationResult.getCaughtErrors());
                result = FileUtilService.getInstance().moveTo(GenericConstant.EMPLOYEE_FILES_RAWTXT_TEMP_PATH, GenericConstant.EMPLOYEE_FILES_RAWTXT_ERROR_PATH, file.getName());
                if (!result) {
                    result = ftpService.moveTo(ftpConfiguration.getFileTempPath(), ftpConfiguration.getFileErrorPath(), file.getName());
                    if (!result)
                        mailService.sendNewsletterMail(getMailSubject(), "Problem occured during file ftp moving", "Problem occured during file moving in ftp. " + file + " was moving from " + ftpConfiguration.getFileTempPath() + " to " + ftpConfiguration.getFileProcessedPath());
                } else {
                    mailService.sendNewsletterMail(getMailSubject(), "Problem occured during file moving", "Problem occured during file moving. " + file + " was moving from " + GenericConstant.EMPLOYEE_FILES_RAWTXT_TEMP_PATH + " to " + GenericConstant.EMPLOYEE_FILES_RAWTXT_ERROR_PATH);
                }
            }
        }
        return rawEmployeeWithValidationResults;
    }

    public List<File> getDownloadedFiles(List<FTPFile> ftpFiles) {
        List<File> files = new ArrayList<File>();
        for (FTPFile ftpFile : ftpFiles.stream().sorted((f1, f2) -> Integer.compare(f1.getFileUploadedDateAsInt(),
                f2.getFileUploadedDateAsInt())).collect(Collectors.toList())) {
            files.add(FileUtilService.getInstance().getFile(GenericConstant.WORKLOAD_REPORTS_RAWTXT_TEMP_PATH + ftpFile.getFileName()));
        }
        return files;
    }

    private RawEmployeeWithValidationResult parseContent(BufferedReader bufferedReader) {
        log.info("Started to importing Employee Data");
        BufferedReader br = bufferedReader;//FileUtilService.getInstance().getFile(classPathFilePattern).getFileContent();
        String line = ""; //lineDelimiter
        String cvsSplitBy = ";"; //fieldDelimiter
        RawEmployeeWithValidationResult rawWorkloadWithValidationResult = new RawEmployeeWithValidationResult();
        List<RawEmployee> rawWorkloadList = new ArrayList<RawEmployee>();
        RawEmployee rawEmployee;
        boolean hasInvalidatedData = false;
        String caughtErrors = "";
        try {
            while ((line = br.readLine()) != null) {

                String[] properties = line.split(cvsSplitBy);

                rawEmployee = new RawEmployee();
                rawEmployee.settNumber(properties[0]);
                rawEmployee.setName(properties[1]);
                rawEmployee.setMiddleName(properties[2]);
                rawEmployee.setSurname(properties[3]);
                rawEmployee.setPositionCode(properties[4]);
                rawEmployee.setPositionDescription(properties[5]);
                rawEmployee.setOrganizationCode(properties[6]);
                rawEmployee.setOrganizationName(properties[7]);
                rawEmployee.setRole(properties[8]);
                rawEmployee.setEmail(properties[9]);
                rawEmployee.setNetid(properties[10]);
                rawEmployee.setFacultyCode(properties[11]);
                rawEmployee.setFacultyName(properties[12]);
                rawEmployee.setOrganizationShortname(properties[13]);

                Set<ConstraintViolation<RawEmployee>> constraintViolations = validator.validate(rawEmployee);
                if (constraintViolations.size() > 0) {
                    hasInvalidatedData = true;
                    caughtErrors = "Here is the data that has an issue \n";
                    caughtErrors = caughtErrors + rawEmployee.toHTML();
                    caughtErrors = caughtErrors + "\n  Following reason(s) ; ";
                    for (ConstraintViolation<RawEmployee> s : constraintViolations) {
                        caughtErrors = caughtErrors + "\n " + s.getPropertyPath() + " - " + s.getMessage(); // NOTIFICATION EMAIL WILL BE SENT ALONGSIDE WITH THE ERROR CODES.

                    }
                    log.error(caughtErrors);
                    break;
                } else {
                    rawWorkloadList.add(rawEmployee); // EVEN GETTING AN ERROR CAUSE IT TO CANCEL THE LOOP AND NOTIFY THE ADMINS
                }
            }
            log.info("Finished importing");

        } catch (IOException e) {
            log.error("Error occured during importing");
            log.error(e.toString());
            hasInvalidatedData = true;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.warn(e.toString());
                }
            }
        }
        rawWorkloadWithValidationResult.setCaughtErrors(caughtErrors);
        rawWorkloadWithValidationResult.setHasInvalidatedData(hasInvalidatedData);
        rawWorkloadWithValidationResult.setRawWorkloadList(rawWorkloadList);
        return rawWorkloadWithValidationResult;
    }


    public String getPrefixNameOfFTPFile() {
        return "Employee_List_";
    }
}
