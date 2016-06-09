package com.jackalhan.ualr.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by txcakaloglu on 6/9/16.
 */
public class DateUtilService {
    private final Logger log = LoggerFactory.getLogger(DateUtilService.class);


    private static DateUtilService singleton;

    public DateUtilService() {
    }

    public static synchronized DateUtilService getInstance() {
        if (singleton == null)
            singleton = new DateUtilService();
        return singleton;
    }

    public LocalDateTime getPreviousDateFromCurrentTime(int numberOfDays) {
        LocalDateTime localDateTime = getCurrentTime();
        return localDateTime.minusDays(numberOfDays);
    }

    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    public boolean isFirstDateIsAfterThanSecondDate(LocalDateTime firstDate, LocalDateTime secondDate) {
        if (firstDate.isAfter(secondDate)) {
            return true;
        } else {
            return false;
        }
    }

    public LocalDateTime convertToLocalDateTime(String pattern, String datetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime = LocalDateTime.parse(datetime, formatter);
        return dateTime;
    }
}
