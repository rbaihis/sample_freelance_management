package com.example.user.services.freelance.utils.email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;


//

@Service
public class EmailSenderWithGmailService implements IEmailSender {



    @Autowired
    private JavaMailSender mailSender;

    @Override
    /** mandatory Fields : toEmail , subject , body ; others re optional can be set to null */
    public void sendBasicEmail(String toEmail, String subject, String body , String emailSentFrom , String cc , String[] filesPath, boolean htmlUsed ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        if (cc != null && !cc.isEmpty())
            message.setFrom(emailSentFrom);
        if (cc != null && !cc.isEmpty())
            message.setCc(cc);

        mailSender.send(message);
    }


    //------------------------------------------------------------------------------------------------------------
    @Override
    /** mandatory Fields : toEmail , subject , body , instance of FileNameAndPathFilled; others re optional can be set to null */
    public void sendEmailWithAttachmentHtmlBodyOrNoOk(String toEmail, String subject, String body,  String emailSentFrom , FileNameAndPath fileNameAndPath , boolean isHtmlBody )  {
        long allowedSize = 24*1048576;;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper= null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            //is body html
            if(isHtmlBody)
                helper.setText(body,true);
            else
                helper.setText(body);
            //emailFrom fakeEmail display
            if(emailSentFrom!=null && emailSentFrom.matches("^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"))
                helper.setFrom(emailSentFrom);
            //adding attachment
            addAttachment(helper,fileNameAndPath,allowedSize);

        } catch (MessagingException e) {
            System.out.println("******exception occurred when creating MimeMessageHelper****");
            System.out.println(e.getMessage());
            return;
        }

        //add attachment ;

        mailSender.send(message);
    }

    //------------------------------------------------------------------------------------------------------

    @Override
    public void sendEmailWithMultipleAttachmentsHtmlBodyOrNoOk(String toEmail, String subject, String body, String emailSentFrom, FileNameAndPath [] fileNamesAndPaths ,  boolean isHtmlBody )  {
        long allowedSize = 24*1048576;;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper= null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            //is body html
            if(isHtmlBody)
                helper.setText(body,true);
            else
                helper.setText(body);

            //sentFrom not mandatory fake sender display
            if(emailSentFrom!=null && emailSentFrom.matches("^[\\w!#$%&'*+/=?^`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"))
                helper.setFrom(emailSentFrom);
            //adding attachment
            if (fileNamesAndPaths!=null)
                for (FileNameAndPath fileNameAndPath : fileNamesAndPaths)
                    addAttachment(helper,fileNameAndPath,allowedSize);

        } catch (MessagingException e) {
            System.out.println("******exception occurred when creating MimeMessageHelper****");
            System.out.println(e.getMessage());
            return;
        }

        //add attachment ;

        mailSender.send(message);
    }

    //-----********PrivateMethods*********--------------------------------------------

    public void addAttachment(MimeMessageHelper helper, FileNameAndPath fileNameAndPath, long allowedSize){
        if( !(fileNameAndPath!=null && fileNameAndPath.getPath()!=null && fileNameAndPath.getFileName()!=null ))
            return;
        FileSystemResource file = new FileSystemResource(new File(fileNameAndPath.getPath()));
        if(allowedSize<file.getFile().length()){
            System.out.println("fileCan't be added exceeded the allowed size of attachment");
            return;
        }
        allowedSize-=file.getFile().length();
        try {
            helper.addAttachment(fileNameAndPath.getFileName(), file);
        } catch (MessagingException e) {
            System.out.println("******exception occurred when Adding Attachment ***");
            System.out.println(e.getMessage());
        }
    }




}


