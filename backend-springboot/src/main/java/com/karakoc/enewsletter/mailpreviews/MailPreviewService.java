package com.karakoc.enewsletter.mailpreviews;

public interface MailPreviewService {
    MailPreview createMailPreview(CreateMailPreviewRequest r);
    MailPreview getMailPreview(String mailPreviewId,String ownerId);
    MailPreview getAllMyMailPreviews(String ownerId);
    void deleteMailPreview(String ownerId,String mailPreviewId);
    MailPreview updateMailPreview(UpdateMailPreviewRequest r);

}
