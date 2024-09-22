package com.karakoc.sofra.newsletters;

import lombok.Data;

import java.util.List;

@Data
public class SendMailResponse {
    private int successCount;
    private List<String> failedEmails;
}
