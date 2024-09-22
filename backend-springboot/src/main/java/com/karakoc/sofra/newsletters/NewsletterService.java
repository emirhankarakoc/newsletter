package com.karakoc.sofra.newsletters;

import com.karakoc.sofra.gmail.SendMailRequest;
import com.karakoc.sofra.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

public interface NewsletterService {
    Newsletter createProject(String ownerUserId, CreateNewsletterRequest r) throws IOException;

    List<Newsletter> getMyProjects(String ownerUserId);
    ResponseEntity sendMessageToSubscribers(String userId,String newsletterId, String accessToken,String subject,String htmlContent) throws IOException;
    NewsletterManager.NewsletterResponse getNewsletterById(String id);
}
