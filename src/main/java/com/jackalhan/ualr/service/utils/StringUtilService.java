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
            return true;
        return false;
    }
}
