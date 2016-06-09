package com.jackalhan.ualr.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jackalhan on 4/19/16.
 */
@Component
public class StringUtilService {

    private final Logger log = LoggerFactory.getLogger(StringUtilService.class);

    private static StringUtilService singleton;

    private StringUtilService(){ }

    public static synchronized StringUtilService getInstance( ) {
        if (singleton == null)
            singleton=new StringUtilService();
        return singleton;
    }

    public boolean isEmpty(String text )
    {
        if(text != null && !text.isEmpty())
            return false;
        return true;
    }

    public String switchText (String text, String splitChar)
    {
        log.debug(" Given text " + text + ", Given Split Char " + splitChar);
        String switchedText = "";
        if(!isEmpty(text)) {
            String[] temp =text.split(splitChar);
            switchedText = temp[1].trim() + " " + temp[0].trim();
        }
        log.debug(" Switched text " + switchedText + ", Given Split Char " + splitChar);
        return switchedText;
    }

    public boolean isANumber (String text)
    {
        boolean result = true;
        try
        {
            int test = Integer.valueOf(text);

        }catch (Exception ex)
        {
            result = false;
        }
        return result;
    }

    public String dateToTargetFormat(String date, String oldFormat, String targetFormat)
    {

        SimpleDateFormat sdfO= new SimpleDateFormat(oldFormat);
        SimpleDateFormat sdfT = new SimpleDateFormat(targetFormat);
        Date dateTemp = null;
        String formattedDate = "";
        try {
            dateTemp = sdfO.parse(date);
        } catch (ParseException e) {
            log.error(e.toString());
        }
        if (dateTemp != null) {
            formattedDate = sdfT.format(dateTemp);
        }
      return formattedDate;
    }


}
