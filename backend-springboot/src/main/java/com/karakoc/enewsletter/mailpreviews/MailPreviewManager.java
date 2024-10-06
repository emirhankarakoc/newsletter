package com.karakoc.enewsletter.mailpreviews;

public class MailPreviewManager implements MailPreviewService{
    @Override
    public MailPreview createMailPreview(CreateMailPreviewRequest r) {
        return null;
    }

    @Override
    public MailPreview getMailPreview(String mailPreviewId, String ownerId) {
        return null;
    }

    @Override
    public MailPreview getAllMyMailPreviews(String ownerId) {
        return null;
    }

    @Override
    public void deleteMailPreview(String ownerId, String mailPreviewId) {

    }

    @Override
    public MailPreview updateMailPreview(UpdateMailPreviewRequest r) {
        return null;
    }
}
