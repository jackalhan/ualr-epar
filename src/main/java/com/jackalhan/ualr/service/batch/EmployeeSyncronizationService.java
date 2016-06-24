package com.jackalhan.ualr.service.batch;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.constant.SchedulingConstant;
import com.jackalhan.ualr.domain.*;
import com.jackalhan.ualr.domain.model.Department;
import com.jackalhan.ualr.domain.model.Employee;
import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.enums.EmployeeRoleEnum;
import com.jackalhan.ualr.repository.FacultyRepository;
import com.jackalhan.ualr.service.db.FacultyDBService;
import com.jackalhan.ualr.service.rest.FTPService;
import com.jackalhan.ualr.service.rest.MailService;
import com.jackalhan.ualr.service.utils.FileUtilService;
import com.jackalhan.ualr.service.utils.StringUtilService;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.*;
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
    public void executeService() {
        List<FTPFile> ftpFiles = ftpService.listFiles(getPrefixNameOfFTPFile() + "*.txt");
        File fileToBeProcessing = getLatestDownloadedFile(ftpFiles);
        moveNonUsedFiles(ftpFiles, fileToBeProcessing);
        RawEmployeeWithValidationResult rawEmployeeWithValidationResults = processAndValidateFile(fileToBeProcessing);
        List<TypeSafeRawEmployee> typeSafeRawEmployeeList = convertRawToTypeSafeData(rawEmployeeWithValidationResults.getRawEmployeeList());
       /* if (rawEmployeeWithValidationResults.size() == 0) {
            mailService.sendNewsletterMail(getMailSubject(), "No file found to be executed", "There is no valid file found based on expected pattern " + getPrefixNameOfFTPFile() + " in FTP server");
        } else {
            result = generateExcelContent(simplifiedWorkloadList, importedFileDate, importedFileTerm);
            if (result) {
                mailService.sendNewsletterMail(getMailSubject(), "It was completed ", "Excel files are generated based on following file " + file + ". You can view all generated files by clicking following link......");

            } else {
                mailService.sendNewsletterMail(NEW_MAIL_SUBJECT, "Problem occured during excel generating", "Excel generating based on following file " + file + " is failed. ");
            }


        }*/
    }

    public void processEmployee(List<TypeSafeRawEmployee> typeSafeRawEmployeeList, File importedFTPFile) {

        /*Map<Pair<String, String>, List<TypeSafeRawEmployee>>  distinctFaculties = typeSafeRawEmployeeList.stream()
                .collect(Collectors.groupingBy(p -> Pair.of(p.getFacultyCode(), p.getFacultyName())));
*/

        for (TypeSafeRawEmployee typeSafeRawEmployee : typeSafeRawEmployeeList) {

            if (typeSafeRawEmployee.getRole().equalsIgnoreCase(EmployeeRoleEnum.DEAN.toString())) {
                Faculty faculty = new Faculty();
                faculty.setName(typeSafeRawEmployee.getFacultyName());
                faculty.setCode(typeSafeRawEmployee.getFacultyCode());
                faculty.setShortName(typeSafeRawEmployee.getPositionDescription().substring(typeSafeRawEmployee.getPositionDescription().lastIndexOf("-" + 1)).trim());
                facultyDBService.createFacultyIfNotFound(faculty);
            }
        }


        for (Faculty faculty : facultyDBService.listAll()) {
            for (TypeSafeRawEmployee typeSafeRawEmployee : typeSafeRawEmployeeList) {
                if (faculty.getCode().equalsIgnoreCase(typeSafeRawEmployee.getFacultyCode())) {
                    if (typeSafeRawEmployee.getRole().equalsIgnoreCase(EmployeeRoleEnum.CHAIR.toString())) {
                        Department department = new Department();
                        department.setCode(typeSafeRawEmployee.getOrganizationShortname());
                        department.setDescription(typeSafeRawEmployee.getOrganizationName());
                        department.setFaculty(faculty);
                        departmentDBService.createDepartmentIfNotFound(department, faculty);
                    }
                }
            }
        }

        for (Department department : departmentDBService.listAll()) {
            for (TypeSafeRawEmployee typeSafeRawEmployee : typeSafeRawEmployeeList) {
                if (department.getCode().equalsIgnoreCase(typeSafeRawEmployee.getOrganizationShortname())) {
                    Employee employee = new Employee();

                    employee.setName(typeSafeRawEmployee.getName());
                    employee.setSurname(typeSafeRawEmployee.getSurname());
                    employee.setMiddleName(typeSafeRawEmployee.getMiddleName());
                    employee.settNumber(typeSafeRawEmployee.gettNumber());
                    employee.setEmail(typeSafeRawEmployee.getEmail());
                    employee.setNetid(typeSafeRawEmployee.getNetid());
                    employee.setPositionCode(typeSafeRawEmployee.getPositionCode());
                    employee.setPositionDescription(typeSafeRawEmployee.getPositionDescription());
                    employee.setRole(typeSafeRawEmployee.getRole());
                    employee.setSourceDataName(importedFTPFile.getName());
                    employee.setDepartment(department);
                    employeeDBService.createFacultyIfNotFound(employee);
                }
            }
        }


        // DEPARTMENT CHAIR, DEKAN VE DIGER PERSONELIN GIRISI YAPILIRKEN, OLMAYANLARIN AKTIF OLMAMASI GEREKIYOR.
        // BIR COK KAYIT VARSA BUNLARINDA KONTROLU YAPILACAK.



/*        //if (typeSafeRawEmployee.getRole().equalsIgnoreCase(EmployeeRoleEnum.CHAIR.toString())) {
        Department department = new Department();
        department.set

        if (typeSafeRawEmployee.getRole().equalsIgnoreCase(EmployeeRoleEnum.DEAN.toString())) {
            Faculty faculty = new Faculty();
            faculty.setName(typeSafeRawEmployee.getFacultyName());
            faculty.setCode(typeSafeRawEmployee.getFacultyCode());
            faculty.setShortName(typeSafeRawEmployee.getPositionDescription().substring(typeSafeRawEmployee.getPositionDescription().lastIndexOf("-" + 1)).trim());
            facultyDBService.createFacultyIfNotFound(faculty);
        }*/
    }


    private List<TypeSafeRawEmployee> convertRawToTypeSafeData(List<RawEmployee> rawEmployees) {

        log.info("Started to convert raw data to type safe data");
        List<TypeSafeRawEmployee> typeSafeRawEmployeeList = null;
        typeSafeRawEmployeeList = new ArrayList<TypeSafeRawEmployee>();
        TypeSafeRawEmployee typeSafeRawEmployee;
        for (RawEmployee rawEmployee : rawEmployees) {
            typeSafeRawEmployee = new TypeSafeRawEmployee();
            typeSafeRawEmployee.settNumber(rawEmployee.gettNumber());
            typeSafeRawEmployee.setName(rawEmployee.getName());
            typeSafeRawEmployee.setMiddleName(rawEmployee.getMiddleName());
            typeSafeRawEmployee.setSurname(rawEmployee.getSurname());
            typeSafeRawEmployee.setPositionCode(rawEmployee.getPositionCode());
            typeSafeRawEmployee.setPositionDescription(rawEmployee.getPositionDescription());
            typeSafeRawEmployee.setOrganizationCode(Integer.parseInt(rawEmployee.getOrganizationCode()));
            typeSafeRawEmployee.setOrganizationName(rawEmployee.getOrganizationName());
            typeSafeRawEmployee.setOrganizationShortname(rawEmployee.getOrganizationShortname());
            typeSafeRawEmployee.setRole(rawEmployee.getRole());
            typeSafeRawEmployee.setEmail(rawEmployee.getEmail());
            typeSafeRawEmployee.setNetid(rawEmployee.getNetid());
            typeSafeRawEmployee.setFacultyCode(rawEmployee.getFacultyCode());
            typeSafeRawEmployee.setFacultyName(rawEmployee.getFacultyName());
            typeSafeRawEmployeeList.add(typeSafeRawEmployee);
        }
        log.info("FInish converting raw data to type safe data");
        return typeSafeRawEmployeeList;
    }

    public RawEmployeeWithValidationResult processAndValidateFile(File file) {
        RawEmployeeWithValidationResult rawEmployeeWithValidationResult;
        boolean result;

        BufferedReader bufferedReader = FileUtilService.getInstance().getFileContent(file);
        rawEmployeeWithValidationResult = parseContent(bufferedReader);
        if (!rawEmployeeWithValidationResult.isHasInvalidatedData()) {
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
        return rawEmployeeWithValidationResult;
    }

    public void moveNonUsedFiles(List<FTPFile> ftpFiles, File notIncludedFile) {
        boolean result;
        for (FTPFile ftpFile : ftpFiles) {
            if (!ftpFile.getFileName().equals(notIncludedFile.getName())) {
                result = ftpService.moveTo(ftpConfiguration.getRemoteDirectory(), ftpConfiguration.getFileIgnoredPath(), ftpFile.getFileName());
                if (!result) {
                    mailService.sendNewsletterMail(getMailSubject(), "Problem occured during file ftp moving", "Problem occured during file moving in ftp. " + ftpFile + " was moving from " + ftpConfiguration.getFileTempPath() + " to " + ftpConfiguration.getFileProcessedPath());
                }
            }
        }
    }

    public File getLatestDownloadedFile(List<FTPFile> ftpFiles) {
        File file = null;
        int latestFtpfile = 0;
        String fileName = null;
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.getFileUploadedDateAsInt() >= latestFtpfile) {
                latestFtpfile = ftpFile.getFileUploadedDateAsInt();
                fileName = ftpFile.getFileName();
            }
        }
        file = FileUtilService.getInstance().getFile(GenericConstant.WORKLOAD_REPORTS_RAWTXT_TEMP_PATH + fileName);
        return file;
    }

    private RawEmployeeWithValidationResult parseContent(BufferedReader bufferedReader) {
        log.info("Started to importing Employee Data");
        BufferedReader br = bufferedReader;//FileUtilService.getInstance().getFile(classPathFilePattern).getFileContent();
        String line = ""; //lineDelimiter
        String cvsSplitBy = ";"; //fieldDelimiter
        RawEmployeeWithValidationResult rawWorkloadWithValidationResult = new RawEmployeeWithValidationResult();
        List<RawEmployee> rawEmployeeList = new ArrayList<RawEmployee>();
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
                    rawEmployeeList.add(rawEmployee); // EVEN GETTING AN ERROR CAUSE IT TO CANCEL THE LOOP AND NOTIFY THE ADMINS
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
        rawWorkloadWithValidationResult.setRawEmployeeList(rawEmployeeList);
        return rawWorkloadWithValidationResult;
    }


    public String getPrefixNameOfFTPFile() {
        return "Employee_List_";
    }
}
