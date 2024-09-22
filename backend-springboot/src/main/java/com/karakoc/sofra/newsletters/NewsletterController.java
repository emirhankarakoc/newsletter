package com.karakoc.sofra.newsletters;

import com.karakoc.sofra.exceptions.general.BadRequestException;
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
    @PostMapping( value = "/{id}/sendMessage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendMessageToSubscribers(@AuthenticationPrincipal UserPrincipal principal, @PathVariable String id, @ModelAttribute SendMailRequest r) throws IOException {
        // Dosyanın içeriğini oku
        String htmlContent;
        try {
            htmlContent = new String(r.getHtmlFile().getBytes());


        } catch (IOException e) {
            throw new BadRequestException("HTML dosyası okunamadı: " + e.getMessage());
        }

        // Mail gönderme işlemi
        return newsletterService.sendMessageToSubscribers(principal.getUserId(), id, r.getAccessToken(),r.getSubject(),htmlContent);
    }
    }


