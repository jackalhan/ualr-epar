package com.jackalhan.ualr.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

}
