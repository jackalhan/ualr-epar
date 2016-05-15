package com.jackalhan.ualr.domain;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jackalhan on 5/14/16.
 */
public class Mail {

    private String from;

    private String[] to;

    private String[] cc;

    private String[] bcc;

    private String content;

    private String subject;

    private File[] attachedFiles;

    private boolean isHtml;

    private List<MailInLine> mailInLineList;

    public Mail() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public File[] getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(File[] attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public List<MailInLine> getMailInLineList() {
        return mailInLineList;
    }

    public void setMailInLineList(List<MailInLine> mailInLineList) {
        this.mailInLineList = mailInLineList;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to=" + Arrays.toString(to) +
                ", cc=" + Arrays.toString(cc) +
                ", bcc=" + Arrays.toString(bcc) +
                ", content=" + content +
                ", subject=" + subject +
                ", attachedFiles=" + Arrays.toString(attachedFiles) +
                ", isHtml=" + isHtml +
                ", mailInLineList=" + mailInLineList +
                '}';
    }
}
