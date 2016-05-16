package com.jackalhan.ualr.service.rest;

import com.jackalhan.ualr.constant.GenericConstant;
import com.jackalhan.ualr.config.MailExecutiveConfiguration;
import com.jackalhan.ualr.domain.Mail;
import com.jackalhan.ualr.domain.MailInLine;
import com.jackalhan.ualr.enums.ImageTypeEnum;
import com.jackalhan.ualr.service.utils.StringUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jackalhan on 4/25/16.
 */
@Service
public class MailService {


    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender javaMailService;

    @Autowired
    private MailExecutiveConfiguration mailExecutiveConfiguration;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendNewsletterMail(String subject, String header, String content) {
        String icoresourcename = "eparIco.ico";
        String defaultemailtemplatejpg1 = "PROMO-GREEN2_01_01.jpg";
        String eitlogopng = "EIT-Logo.png";
        String eparlogopng = "eparLogo.png";
        String ualrlogopng = "ualr-logo.png";
        String defaultemailtemplatejpg2 = "PROMO-GREEN2_07.jpg";

        Mail mail = new Mail();
        mail.setFrom(mailExecutiveConfiguration.from);
        mail.setTo(mailExecutiveConfiguration.getTo());
        mail.setBcc(null);
        mail.setCc(mailExecutiveConfiguration.getDeveloper());
        mail.setSubject(subject + " @UALR-EPAR");
        mail.setHtml(true);
        final Context context = new Context(Locale.ENGLISH);
        context.setVariable("topic", header);
        context.setVariable("content", content);
        context.setVariable("icoresourcename", icoresourcename);
        context.setVariable("defaultemailtemplatejpg1", defaultemailtemplatejpg1);
        context.setVariable("eitlogopng", eitlogopng);
        context.setVariable("eparlogopng", eparlogopng);
        context.setVariable("ualrlogopng", ualrlogopng);
        context.setVariable("defaultemailtemplatejpg2", defaultemailtemplatejpg2);

        final String htmlContent = this.templateEngine.process("mails/newsletter/newsletter", context);
        mail.setContent(htmlContent);
        List<MailInLine> mailInLineList = new ArrayList<MailInLine>();
        MailInLine mailInLine = new MailInLine(icoresourcename, GenericConstant.RESOURCE_UALR_IMAGE_PATH, ImageTypeEnum.ICON.toString());
        MailInLine mailInLine2 = new MailInLine(defaultemailtemplatejpg1, GenericConstant.RESOURCE_MAIL_IMAGE_PATH, ImageTypeEnum.JPEG.toString());
        MailInLine mailInLine3 = new MailInLine(eitlogopng, GenericConstant.RESOURCE_UALR_IMAGE_PATH, ImageTypeEnum.PNG.toString());
        MailInLine mailInLine4 = new MailInLine(eparlogopng, GenericConstant.RESOURCE_UALR_IMAGE_PATH, ImageTypeEnum.PNG.toString());
        MailInLine mailInLine5 = new MailInLine(ualrlogopng, GenericConstant.RESOURCE_UALR_IMAGE_PATH, ImageTypeEnum.PNG.toString());
        MailInLine mailInLine6 = new MailInLine(defaultemailtemplatejpg2, GenericConstant.RESOURCE_MAIL_IMAGE_PATH, ImageTypeEnum.JPEG.toString());

        mailInLineList.add(mailInLine);
        mailInLineList.add(mailInLine2);
        mailInLineList.add(mailInLine3);
        mailInLineList.add(mailInLine4);
        mailInLineList.add(mailInLine5);
        mailInLineList.add(mailInLine6);

        mail.setMailInLineList(mailInLineList);

        send(mail);
    }

    public void send(Mail mail) {
        try {

            // HTML MAIL MESSAGE
            MimeMessage mimeMessage = javaMailService.createMimeMessage();
            //Pass true flag for multipart message
            MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, true);
            mailMsg.setTo(mail.getTo());
            if (mail.getCc() != null)
                mailMsg.setCc(mail.getCc());

            if (mail.getBcc() != null)
                mailMsg.setBcc(mail.getBcc());

            mailMsg.setText(mail.getContent(), mail.isHtml());
            mailMsg.setReplyTo(mail.getFrom());
            mailMsg.setPriority(1);
            mailMsg.setSubject(mail.getSubject());
            if (mail.getAttachedFiles() != null) {
                int i = 0;
                FileSystemResource fileSystemResource = null;
                for (File file : mail.getAttachedFiles()) {
                    i++;
                    fileSystemResource = new FileSystemResource(file);
                    mailMsg.addAttachment("attachment" + i, file);
                }
            }

            try {
                for (MailInLine mailInLine : mail.getMailInLineList()) {
                    mailMsg.addInline(mailInLine.getFileName(), new FileSystemResource(mailInLine.getFileLocation() + mailInLine.getFileName()), mailInLine.getFileType());
                }

            } catch (Exception ex) {
                log.error(ex.toString());
            }
            javaMailService.send(mimeMessage);


            /* SIMPLE MAIL MESSAGE
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(text);
            javaMailService.send(mailMessage);
            */
        } catch (Exception ex) {
            log.error(ex.toString());
        }
    }

}
