package com.jackalhan.ualr.service.batch;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.constant.SchedulingConstant;
import com.jackalhan.ualr.domain.FTPFile;
import com.jackalhan.ualr.service.db.WorkloadReportDBService;
import com.jackalhan.ualr.service.rest.FTPService;
import com.jackalhan.ualr.service.rest.MailService;
import com.jackalhan.ualr.service.utils.DateUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by txcakaloglu on 6/9/16.
 */
@Component
@Service
public class FileDeleteService extends BatchService {


    @Autowired
    private WorkloadReportService workloadReportBatchService;



    @Scheduled(cron = SchedulingConstant.FILE_DELETE_SERVICE_EXECUTE_CRON)
    public void executeService() {

        List<FTPFile> ftpFiles = ftpService.listFiles(workloadReportBatchService.getPrefixNameOfFTPFile() + "*.txt");
        deleteFiles(ftpFiles);

    }

    public void deleteFiles(List<FTPFile> ftpFiles) {
        try {
            if (ftpFiles != null && !ftpFiles.isEmpty()) {
                //filter out files older than 15 days.
                ftpFiles = ftpFiles.stream().filter(f ->
                        !DateUtilService.getInstance().isFirstDateIsAfterThanSecondDate(
                                DateUtilService.getInstance().convertToLocalDateTime(GenericConstant.SIMPLE_DATE_TIME_DEFAULT_ALL_DATE_FORMAT, f.getFileUploadedDateAsOriginal()),
                                DateUtilService.getInstance().getPreviousDateFromCurrentTime(15)
                        )).collect(Collectors.toList());

                for (FTPFile ftpFile : ftpFiles) {
                    ftpService.delete(ftpFile.getFileName());
                }

            }
        } catch (Exception ex) {
            mailService.sendNewsletterMail(getMailSubject(), getMailSubject(), ex.toString());
            log.error(ex.toString());

        }
    }

    @Override
    public void initialize() {
        setLog(LoggerFactory.getLogger(FileDeleteService.class));
        setMailSubject("Exception while deleting old files from FTP ");
    }
}
