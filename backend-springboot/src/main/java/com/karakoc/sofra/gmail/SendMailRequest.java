package com.karakoc.sofra.gmail;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SendMailRequest {
    private String accessToken;
    private String subject;
    private MultipartFile htmlFile;
}
