package com.karakoc.enewsletter.mailpreviews;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateMailPreviewRequest {
    private String name;
    private String htmlContent;
}
