package com.jackalhan.ualr.domain.validate;

import com.jackalhan.ualr.domain.RawWorkload;
import com.jackalhan.ualr.service.utils.StringUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Locale;

/**
 * Created by jackalhan on 4/29/16.
 */
public class RawWorkloadValidator implements Validator {

    @Autowired
    private MessageSource messageSource;

    private final Logger log = LoggerFactory.getLogger(StringUtilService.class);


    private static RawWorkloadValidator singleton;

    private RawWorkloadValidator() {
    }

    public static synchronized RawWorkloadValidator getInstance() {
        if (singleton == null)
            singleton = new RawWorkloadValidator();
        return singleton;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return RawWorkload.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        String fieldCanNotBeNull = "workloadReport.fieldCanNotBeNull.text";
        String fieldIsOutOfFormat=  "workloadReport.number.fieldIsOutOfFormat.text";
        ValidationUtils.rejectIfEmpty(errors, "instructionType", fieldCanNotBeNull, messageSource.getMessage(fieldCanNotBeNull, new Object[]{"instructiontype"} , Locale.US));
        ValidationUtils.rejectIfEmpty(errors, "instructorTNumber", fieldCanNotBeNull, messageSource.getMessage(fieldCanNotBeNull, new Object[]{"instructorTNumber"} , Locale.US));
        //ValidationUtils.rejectIfEmpty(errors, "instructorNameSurname", "workloadReport.fieldCanNotBeNull.text", new Object[]{"instructorNameSurname"} );
        //ValidationUtils.rejectIfEmpty(errors, "semesterTermCode", "workloadReport.fieldCanNotBeNull.text", new Object[]{"semesterTermCode"} );

        RawWorkload rawWorkload = (RawWorkload) o;
        if(!(rawWorkload.getSemesterTermCode().length() == 6) ||
                (!StringUtilService.getInstance().isANumber(rawWorkload.getSemesterTermCode())))
        {
            errors.rejectValue("semesterTermCode", fieldIsOutOfFormat, messageSource.getMessage(fieldCanNotBeNull, new Object[]{"semesterTermCode",rawWorkload.getSemesterTermCode()} , Locale.US) );
        }

    }
}
