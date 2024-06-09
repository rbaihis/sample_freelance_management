package com.example.user.services.freelance.utils.email;

public interface IEmailSender {
    //------------------------------------------------------------------------------------------------------------

    /** mandatory Fields : toEmail , subject , body ; others re optional can be set to null */
    void sendBasicEmail(String toEmail, String subject, String body, String emailSentFrom, String cc, String[] filesPath, boolean htmlUsed);

    /** mandatory Fields : toEmail , subject , body , instance of FileNameAndPathFilled; others re optional can be set to null */
    void sendEmailWithAttachmentHtmlBodyOrNoOk(String toEmail, String subject, String body, String emailSentFrom, FileNameAndPath fileNameAndPath, boolean isHtmlBody);

    void sendEmailWithMultipleAttachmentsHtmlBodyOrNoOk(String toEmail, String subject, String body, String emailSentFrom, FileNameAndPath[] fileNamesAndPaths, boolean isHtmlBody);


}
