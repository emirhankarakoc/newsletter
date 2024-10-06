package com.karakoc.enewsletter.gmail;

public interface GmailService {
    void sendEmail(String accessToken, String to, String subject, String bodyText);
}
