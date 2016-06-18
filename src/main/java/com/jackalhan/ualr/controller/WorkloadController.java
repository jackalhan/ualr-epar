package com.jackalhan.ualr.controller;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.domain.FTPFile;
import com.jackalhan.ualr.domain.model.Faculty;
import com.jackalhan.ualr.domain.model.WorkloadReport;
import com.jackalhan.ualr.domain.model.WorkloadReportTerm;
import com.jackalhan.ualr.domain.model.WorkloadReportValues;
import com.jackalhan.ualr.repository.WorkloadReportTermRepository;
import com.jackalhan.ualr.service.batch.WorkloadReportService;
import com.jackalhan.ualr.service.db.FacultyDBService;
import com.jackalhan.ualr.service.db.WorkloadReportDBService;
import com.jackalhan.ualr.service.rest.FTPService;
import com.jackalhan.ualr.service.rest.LoginService;
import com.jackalhan.ualr.service.utils.DateUtilService;
import com.jackalhan.ualr.service.utils.FileUtilService;
import com.jackalhan.ualr.service.utils.SummarizeReportService;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import jxl.Sheet;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by jackalhan on 5/15/16.
 */
@Controller
public class WorkloadController extends BaseController{

    @Autowired
    private WorkloadReportDBService workloadReportDBService;

    @Autowired
    private FacultyDBService facultyDBService;

    @Autowired
    private FTPService ftpService;

    @Autowired
    private WorkloadReportService workloadReportBatchService;

    @Autowired
    private SummarizeReportService summarizeReportService;

    public WorkloadController() {
        setLog(LoggerFactory.getLogger(WorkloadController.class));
    }

    @RequestMapping("workload_reports_terms")
    public String workload(Model model, Principal principal) {
        LoginService loginService = new LoginService();
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        List<WorkloadReportTerm> records = workloadReportDBService.listAllWorkloadReportTermsAndGroupByFacultyCodeAndYear();
        model.addAttribute("workloadsReportTerms", records);
        List<FTPFile> ftpFiles = ftpService.listFiles(workloadReportBatchService.getPrefixNameOfFTPFile() + "*.txt");
        if (ftpFiles != null && !ftpFiles.isEmpty()) {
            //filter out files older than 15 days.
            ftpFiles = ftpFiles.stream().filter(f ->
                    DateUtilService.getInstance().isFirstDateIsAfterThanSecondDate(
                            DateUtilService.getInstance().convertToLocalDateTime(GenericConstant.SIMPLE_DATE_TIME_DEFAULT_ALL_DATE_FORMAT, f.getFileUploadedDateAsOriginal()),
                            DateUtilService.getInstance().getPreviousDateFromCurrentTime(15)
                    )).collect(Collectors.toList());

            //sorting based on file date
            ftpFiles = ftpFiles.stream().sorted((f2, f1) -> Integer.compare(f1.getFileUploadedDateAsInt(),
                    f2.getFileUploadedDateAsInt())).collect(Collectors.toList());
        }
        model.addAttribute("ftpWorkloadFiles", ftpFiles);
        return "workload_reports_terms";
    }

    @RequestMapping("importingFTPFile")
    public String importFTPFile(@RequestParam("fileName") String fileName, Model model, Principal principal) throws WriteException, SftpException, JSchException, CloneNotSupportedException, IOException {

        workloadReportBatchService.setFileName(fileName);
        workloadReportBatchService.executeService();
        workload(model, principal);
        return "workload_reports_terms";

    }

    @RequestMapping("workload_reports_faculty_terms")
    public String listWorkloadReportsBasedOnFacultyAndTermsAndFileImportedDate(String facultyCode, int semesterTermYear, Model model, Principal principal) {
        LoginService loginService = new LoginService();
        Faculty faculty = facultyDBService.findByCode(facultyCode);
        List<WorkloadReportTerm> records = workloadReportDBService.listAllTermsBasedOnFacultyAndYear(facultyCode, semesterTermYear);


        model.addAttribute("facultyWorkloadsReportTerms", records);
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        model.addAttribute("facultyName", faculty.getName());
        model.addAttribute("semesterYear", semesterTermYear);
        model.addAttribute("dynamicColumnClassName", calculateDynamicDivSizeOfFacultyName(faculty.getName().length()));

        return "workload_reports_faculty_terms";
    }

    @RequestMapping("workload_reports")
    public String listWorkloadReports(Long workloadReportTermId, String departmentCode, Model model, Principal principal) {
        LoginService loginService = new LoginService();
        WorkloadReportTerm workloadReportTerm = workloadReportDBService.listOneWorkloadReportTermBasedOnId(workloadReportTermId);
        Faculty faculty = facultyDBService.findByCode(workloadReportTerm.getFaculty().getCode());
        List<WorkloadReport> workloadReports = workloadReportDBService.listAllWorkloadReportsBasedOnTermIdAndDepartmentCode(workloadReportTermId, departmentCode);

        model.addAttribute("workloadReports", workloadReports);
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        model.addAttribute("facultyName", faculty.getName());
        model.addAttribute("semesterYear", workloadReportTerm.getSemesterYear());
        model.addAttribute("semesterTermName", workloadReportTerm.getSemesterTerm());
        model.addAttribute("importedFileDate", workloadReportTerm.getImportedFileDate());
        model.addAttribute("dynamicColumnClassName", calculateDynamicDivSizeOfFacultyName(faculty.getName().length()));

        return "workload_reports";
    }


    @RequestMapping("workload_reports_department")
    public String listWorkloadReportsGroupedByDepartment(Long workloadReportTermId, Model model, Principal principal) {
        LoginService loginService = new LoginService();
        WorkloadReportTerm workloadReportTerm = workloadReportDBService.listOneWorkloadReportTermBasedOnId(workloadReportTermId);
        Faculty faculty = facultyDBService.findByCode(workloadReportTerm.getFaculty().getCode());
        List<WorkloadReport> workloadReports = workloadReportDBService.listAllGroupByDepartmentNameAndCodeOrderedByDepartmentNameBasedOnTermId(workloadReportTermId);

        model.addAttribute("departmentListOfWorkloadLoadReports", workloadReports);
        model.addAttribute("username", loginService.getUserName(principal));
        model.addAttribute("userroles", loginService.getUserRoles(principal));
        model.addAttribute("facultyName", faculty.getName());
        model.addAttribute("semesterYear", workloadReportTerm.getSemesterYear());
        model.addAttribute("semesterTermName", workloadReportTerm.getSemesterTerm());
        model.addAttribute("importedFileDate", workloadReportTerm.getImportedFileDate());
        model.addAttribute("workloadReportTermId", workloadReportTermId);
        model.addAttribute("dynamicColumnClassName", calculateDynamicDivSizeOfFacultyName(faculty.getName().length()));

        return "workload_reports_department";
    }

    @RequestMapping(value = "workload_reports_downloaded", method = RequestMethod.GET)
    public String getWorkloadReport(@RequestParam("workloadReportId") Long workloadReportId, @RequestParam("workloadReportTermId") Long workloadReportTermId, HttpServletResponse httpServletResponse, Model model, Principal principal) {
        String reportName = null;
        try {
            LoginService loginService = new LoginService();
            WorkloadReportTerm workloadReportTerm = workloadReportDBService.listOneWorkloadReportTermBasedOnId(workloadReportTermId);
            Faculty faculty = facultyDBService.findByCode(workloadReportTerm.getFaculty().getCode());
            WorkloadReport workloadReport = workloadReportDBService.listOneWorkloadReportsBasedOnId(workloadReportId);
            reportName = workloadReport.getReportName();
            model.addAttribute("username", loginService.getUserName(principal));
            model.addAttribute("userroles", loginService.getUserRoles(principal));
            model.addAttribute("facultyName", faculty.getName());
            model.addAttribute("semesterYear", workloadReportTerm.getSemesterYear());
            model.addAttribute("semesterTermName", workloadReportTerm.getSemesterTerm());
            model.addAttribute("dynamicColumnClassName", calculateDynamicDivSizeOfFacultyName(faculty.getName().length()));
            model.addAttribute("instructorNameSurname", workloadReport.getInstructorNameSurname());
            model.addAttribute("departmentName", workloadReport.getDepartmentName());
            model.addAttribute("reportName", workloadReport.getReportName());

            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + reportName);
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.getOutputStream().write(workloadReport.getReport());
            httpServletResponse.flushBuffer();

        } catch (IOException ex) {
            log.error("Error writing file to output stream. Filename was '{}'", reportName, ex);
        }
        return "workload_reports_downloaded";

    }

    @RequestMapping(value = "allWorkloadOfEachDepartmentAsAZipBasedOnDepartmentCode", method = RequestMethod.GET)
    public String getAllWorkloadOfEachDepartmentAsAZipBasedOnDepartmentCode(@RequestParam("workloadReportTermId") Long workloadReportTermId, @RequestParam("departmentCode") String departmentCode, HttpServletResponse httpServletResponse, Model model, Principal principal) {
        String zipFileName = null;
        try {

            WorkloadReportTerm workloadReportTerm = workloadReportDBService.listOneWorkloadReportTermBasedOnId(workloadReportTermId);
            Faculty faculty = facultyDBService.findByCode(workloadReportTerm.getFaculty().getCode());
            List<WorkloadReport> workloadReports = workloadReportDBService.listAllWorkloadReportsBasedOnWorkloadReportTermIdAndDepartmentCode(workloadReportTerm.getId(), departmentCode);

            zipFileName = faculty.getShortName() + "_" + departmentCode + "_AllLoads_" + workloadReportTerm.getSemesterYear() + workloadReportTerm.getSemesterTerm() + "_" + workloadReportTerm.getImportedFileDate() + ".zip";

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
            ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

            for (WorkloadReport workloadReport : workloadReports) {
                zipOutputStream.putNextEntry(new ZipEntry(workloadReport.getReportName()));
                zipOutputStream.write(workloadReport.getReport());
                zipOutputStream.closeEntry();
            }

            if (zipOutputStream != null) {
                zipOutputStream.finish();
                zipOutputStream.flush();
                IOUtils.closeQuietly(zipOutputStream);
            }
            IOUtils.closeQuietly(bufferedOutputStream);
            IOUtils.closeQuietly(byteArrayOutputStream);

            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
            httpServletResponse.setContentType("application/zip");
            httpServletResponse.getOutputStream().write(byteArrayOutputStream.toByteArray());
            httpServletResponse.flushBuffer();

        } catch (IOException ex) {
            log.error("Error writing file to output stream. Filename was '{}'", zipFileName, ex);
        }
        return "workload_reports_downloaded";

    }

    @RequestMapping(value = "summaryReportOfAllWorkloadsOfSelectedImportedFileDateforAllDepartment", method = RequestMethod.GET)
    public String getSummaryReportOfAllWorkloadsOfSelectedImportedFileDateforAllDepartment(@RequestParam("workloadReportTermId") Long workloadReportTermId, HttpServletResponse httpServletResponse, Model model, Principal principal) {
        String reportName = null;
        try {
            WorkloadReportTerm workloadReportTerm = workloadReportDBService.listOneWorkloadReportTermBasedOnId(workloadReportTermId);
            Faculty faculty = facultyDBService.findByCode(workloadReportTerm.getFaculty().getCode());
            List<WorkloadReport> groupWorkloadReports = workloadReportDBService.listAllGroupByDepartmentNameAndCodeOrderedByDepartmentNameBasedOnTermId(workloadReportTerm.getId());

            org.apache.commons.io.output.ByteArrayOutputStream byteArrayOutputStream = summarizeReportService.generateExcelDocument(faculty, workloadReportTerm, groupWorkloadReports);
            reportName = messageSource.getMessage("summary_report.faculty.file.name",
                    new Object[]{faculty.getShortName(),
                                 workloadReportTerm.getSemesterYear() + workloadReportTerm.getSemesterTerm(),
                                 workloadReportTerm.getImportedFileDate()}, Locale.US);

            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + reportName);
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.getOutputStream().write(byteArrayOutputStream.toByteArray());
            httpServletResponse.flushBuffer();

        } catch (IOException ex) {
            log.error("Error writing file to output stream. Filename was '{}'", reportName, ex);
        }
        return "workload_reports_downloaded";

    }

    @RequestMapping(value = "summaryReportOfAllWorkloadsOfSelectedImportedFileDateforSelectedDepartment", method = RequestMethod.GET)
    public String getSummaryReportOfAllWorkloadsOfSelectedImportedFileDateforSelectedDepartment(@RequestParam("workloadReportTermId") Long workloadReportTermId, @RequestParam("departmentCode") String departmentCode, HttpServletResponse httpServletResponse, Model model, Principal principal) {
        String reportName = null;
        try {
            WorkloadReportTerm workloadReportTerm = workloadReportDBService.listOneWorkloadReportTermBasedOnId(workloadReportTermId);
            Faculty faculty = facultyDBService.findByCode(workloadReportTerm.getFaculty().getCode());
            List<WorkloadReport> groupWorkloadReports = workloadReportDBService.listAllGroupByDepartmentNameAndCodeOrderedByDepartmentNameBasedOnTermIdAndDepartmentCode(workloadReportTerm.getId(), departmentCode);

            org.apache.commons.io.output.ByteArrayOutputStream byteArrayOutputStream = summarizeReportService.generateExcelDocument(faculty, workloadReportTerm, groupWorkloadReports);
            reportName = messageSource.getMessage("summary_report.faculty.department.file.name",
                    new Object[]{faculty.getShortName(),
                            departmentCode,
                            workloadReportTerm.getSemesterYear() + workloadReportTerm.getSemesterTerm(),
                            workloadReportTerm.getImportedFileDate()}, Locale.US);

            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + reportName);
            httpServletResponse.setContentType("application/vnd.ms-excel");
            httpServletResponse.getOutputStream().write(byteArrayOutputStream.toByteArray());
            httpServletResponse.flushBuffer();

        } catch (IOException ex) {
            log.error("Error writing file to output stream. Filename was '{}'", reportName, ex);
        }
        return "workload_reports_downloaded";

    }


    @RequestMapping(value = "allWorkloadsOfEachImportedFileDateforAllDepartmentAsAZipBasedOnImportedFileDate", method = RequestMethod.GET)
    public String getAllWorkloadsOfEachImportedFileDateforAllDepartmentAsAZipBasedOnImportedFileDate(@RequestParam("workloadReportTermId") Long workloadReportTermId, HttpServletResponse httpServletResponse, Model model, Principal principal) {
        String zipFileName = null;
        try {

            WorkloadReportTerm workloadReportTerm = workloadReportDBService.listOneWorkloadReportTermBasedOnId(workloadReportTermId);
            Faculty faculty = facultyDBService.findByCode(workloadReportTerm.getFaculty().getCode());
            List<WorkloadReport> workloadReports = workloadReportDBService.listAllWorkloadReportsBasedOnWorkloadReportTermId(workloadReportTerm.getId());

            zipFileName = faculty.getShortName() + "_AllDepartments_AllLoads_" + workloadReportTerm.getSemesterYear() + workloadReportTerm.getSemesterTerm() + "_" + workloadReportTerm.getImportedFileDate() + ".zip";

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
            ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

            for (WorkloadReport workloadReport : workloadReports) {
                zipOutputStream.putNextEntry(new ZipEntry(workloadReport.getReportName()));
                zipOutputStream.write(workloadReport.getReport());
                zipOutputStream.closeEntry();
            }

            if (zipOutputStream != null) {
                zipOutputStream.finish();
                zipOutputStream.flush();
                IOUtils.closeQuietly(zipOutputStream);
            }
            IOUtils.closeQuietly(bufferedOutputStream);
            IOUtils.closeQuietly(byteArrayOutputStream);

            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + zipFileName);
            httpServletResponse.setContentType("application/zip");
            httpServletResponse.getOutputStream().write(byteArrayOutputStream.toByteArray());
            httpServletResponse.flushBuffer();

        } catch (IOException ex) {
            log.error("Error writing file to output stream. Filename was '{}'", zipFileName, ex);
        }
        return "workload_reports_downloaded";

    }

    private String calculateDynamicDivSizeOfFacultyName(int facultyNameLength) {
        double block = facultyNameLength / 9;
        int partSize = (int) Math.ceil(block);
        return "col-md-" + partSize;
    }
}
