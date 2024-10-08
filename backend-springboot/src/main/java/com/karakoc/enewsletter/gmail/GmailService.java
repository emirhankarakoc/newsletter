package com.karakoc.enewsletter.gmail;

import com.karakoc.enewsletter.customers.Customer;

public interface GmailService {
    void sendEmail(String accessToken, Customer customer, String subject, String bodyText);
}
