package com.karakoc.sofra.newsletters;

import com.karakoc.sofra.cloudinary.entity.Image;
import com.karakoc.sofra.cloudinary.repository.ImageRepository;
import com.karakoc.sofra.cloudinary.service.CloudinaryService;
import com.karakoc.sofra.customers.Customer;
import com.karakoc.sofra.exceptions.general.BadRequestException;
import com.karakoc.sofra.exceptions.general.ForbiddenException;
import com.karakoc.sofra.exceptions.general.NotfoundException;
import com.karakoc.sofra.gmail.GmailService;
import com.karakoc.sofra.gmail.SendMailRequest;
import com.karakoc.sofra.user.User;
import com.karakoc.sofra.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class NewsletterManager implements NewsletterService {
    private final NewsletterRepository newsletterRepository;
    private final UserRepository userRepository;
    private final GmailService gmailService;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public Newsletter createProject(String ownerUserId, CreateNewsletterRequest r) throws IOException {
        Newsletter newsletter = new Newsletter();

        try {
            if (r.getFile().isEmpty()) {
                throw new BadRequestException("File is missing or empty.");
            }

            // Cloudinary'ye yükle
            Map uploadResult = cloudinaryService.upload(r.getFile());

            Image   image = new Image();
            image.setId(UUID.randomUUID().toString());
            image.setImageUrl((String) uploadResult.get("url"));
            image.setImageId(uploadResult.get("public_id").toString());
            image.setName((String) uploadResult.get("original_filename"));
            imageRepository.save(image);

            newsletter.setId(UUID.randomUUID().toString());
            newsletter.setName(r.getName());
            newsletter.setDescription(r.getDescription());
            newsletter.setImageUrl(image.getImageUrl());
            newsletter.setImageId(image.getId());
            newsletter.setCustomers(new ArrayList<>());
            newsletter.setOwnerUserId(ownerUserId);
            newsletterRepository.save(newsletter);


        } catch (IOException e) {
            log.error("Cloudinary upload failed", e);
            throw new BadRequestException("File upload failed: " + e.getMessage());        }
        return newsletter;


    }

    @Override
    public List<Newsletter> getMyProjects(String ownerUserId) {
        return newsletterRepository.findAllByOwnerUserId(ownerUserId);
    }

    @Override
    public ResponseEntity sendMessageToSubscribers(String userId, String newsletterId, String accessToken2,String subject2,String htmlContent2) {
        Newsletter newsletter = validateNewsletter(newsletterId);
        User user = validateUser(userId);
        validateUserAccessToken(user);

        int sentMailCounter = 0;
        List<String> failedEmails = new ArrayList<>();

        if (newsletter.getOwnerUserId().equals(user.getId())) {
            String accessToken = user.getOauth2().getAccess_token();
            List<Customer> subscribers = newsletter.getCustomers();

            for (Customer customer : subscribers) {
                try {
                    gmailService.sendEmail(accessToken, customer.getEmail(), subject2, htmlContent2);
                    sentMailCounter++;
                } catch (Exception e) {
                    failedEmails.add(customer.getEmail());
                }
            }
        } else {
            throw new ForbiddenException("Forbidden.");
        }

        SendMailResponse response = new SendMailResponse();
        response.setSuccessCount(sentMailCounter);
        response.setFailedEmails(failedEmails);

        return ResponseEntity.ok(response);
    }

    private void validateUserAccessToken(User user) {
        if (user.getOauth2() == null || user.getOauth2().getAccess_token().isEmpty()) {
            throw new BadRequestException("You must connect your Gmail account.");
        }
    }

    private Newsletter validateNewsletter(String newsletterId) {
        return newsletterRepository.findById(newsletterId)
                .orElseThrow(() -> new NotfoundException("Newsletter not found."));
    }

    private User validateUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotfoundException("User not found."));
    }

    public record NewsletterResponse(String id, String name, String description, String imageUrl) {}

    @Override
    public NewsletterResponse getNewsletterById(String id) {
        Newsletter newsletter = newsletterRepository.findById(id).orElseThrow(()->new NotfoundException("Newsletter not found."));
        // Newsletter nesnesini NewsletterResponse'a dönüştür
        return new NewsletterResponse(
                newsletter.getId(),
                newsletter.getName(),
                newsletter.getDescription(),
                newsletter.getImageUrl()
        );
    }
}
