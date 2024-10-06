package com.karakoc.enewsletter.gmail;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SendMailRequest {
    private String accessToken;
    private String subject;
    private MultipartFile htmlFile;
}
