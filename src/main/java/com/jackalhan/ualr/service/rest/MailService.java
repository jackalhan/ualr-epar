package com.jackalhan.ualr.service.rest;

import com.jackalhan.ualr.service.utils.StringUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by jackalhan on 4/25/16.
 */
@Service
public class MailService {


    private final Logger log = LoggerFactory.getLogger(StringUtilService.class);

    @Autowired
    private  JavaMailSender  javaMailService;


    public void send(String from, String[] to, String[] cc, String[] bcc,  String content, String subject , File file, boolean isHtml) {
        try {

            // HTML MAIL MESSAGE
            MimeMessage mimeMessage = javaMailService.createMimeMessage();
            //Pass true flag for multipart message
            MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, true);
            mailMsg.setTo(to);
            if (cc != null )
                mailMsg.setCc(cc);

            if (bcc != null)
                mailMsg.setBcc(bcc);

            mailMsg.setText(content, isHtml);
            mailMsg.setReplyTo(from);
            mailMsg.setPriority(1);
            mailMsg.setSubject(subject);
            if (file != null)
            {
                FileSystemResource fileSystemResource = new FileSystemResource(file);
                mailMsg.addAttachment("att1",file);
            }
            javaMailService.send(mimeMessage);


            /* SIMPLE MAIL MESSAGE
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            javaMailService.send(mailMessage);
            */
        }
        catch (Exception ex)
        {
            log.error(ex.toString());
        }
    }

}
