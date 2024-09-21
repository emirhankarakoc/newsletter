package com.karakoc.sofra.newsletters;

import com.karakoc.sofra.gmail.SendMailRequest;
import com.karakoc.sofra.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/newsletters")
@AllArgsConstructor
public class NewsletterController {
private final NewsletterService newsletterService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Newsletter postProject(@AuthenticationPrincipal UserPrincipal principal, @ModelAttribute CreateNewsletterRequest r) throws IOException {
        return newsletterService.createProject(principal.getUserId(),r);
    }
    @GetMapping("{id}")
    public NewsletterManager.NewsletterResponse getNewsletterById(@PathVariable String id){
        return newsletterService.getNewsletterById(id);
    }

    @GetMapping("/my")
    public List<Newsletter> getMyProjects(@AuthenticationPrincipal UserPrincipal principal) {
        return newsletterService.getMyProjects(principal.getUserId());
    }

    @PostMapping("/{id}/sendMessage")
    public ResponseEntity sendMessageToSubscribers(@AuthenticationPrincipal UserPrincipal principal, @PathVariable String id, @RequestBody SendMailRequest r) {
            return newsletterService.sendMessageToSubscribers(principal.getUserId(),id,r);
    }

}
