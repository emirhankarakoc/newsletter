package com.karakoc.enewsletter.gmail;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.karakoc.enewsletter.customers.Customer;
import com.karakoc.enewsletter.exceptions.general.NotfoundException;
import com.karakoc.enewsletter.newsletters.Newsletter;
import com.karakoc.enewsletter.newsletters.NewsletterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Multipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

@Service
@AllArgsConstructor
public class GmailManager implements GmailService {
    private final NewsletterRepository newsletterRepository;

    private static final String APPLICATION_NAME = "newsletter-service";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // Gmail API'yi başlatmak için OAuth2 access token kullanıyoruz
    private Gmail getGmailService(String accessToken) {
        Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(accessToken);

        return new Gmail.Builder(new NetHttpTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // Kullanıcı adına e-posta gönderme
    public void sendEmail(String accessToken, Customer customer, String subject, String htmlContent) {
        try {
            Newsletter news = newsletterRepository.findById(customer.getNewsletterId()).orElseThrow(()->new NotfoundException("Newsletter not found. But this error message shouldn't saw"));
            // Gmail servisini başlat
            Gmail service = getGmailService(accessToken);

            // Kullanıcı email'ini al
            String userEmail = getUserEmailFromToken(service);

            htmlContent = htmlContent.replace("ELECTRONICNEWSLETTERSERVICE_USERNAME", customer.getName());
            htmlContent = htmlContent.replace("ELECTRONICNEWSLETTERSERVICE_USERMAIL", customer.getEmail());
            htmlContent = htmlContent.replace("ELECTRONICNEWSLETTER_USERID",customer.getId());
            htmlContent = htmlContent.replace("ELECTRONICNEWSLETTER_NEWSLETTERIMAGE",news.getImageUrl());
            htmlContent = htmlContent.replace("ELECTRONICNEWSLETTER_NEWSLETTERID",news.getId());
            htmlContent = htmlContent.replace("ELECTRONICNEWSLETTER_NEWSLETTERCUSTOMER_COUNT",news.getCustomers().size() + "");
            htmlContent = htmlContent.replace("ELECTRONICNEWSLETTER_NEWSLETTERID",news.getId());
            htmlContent = htmlContent.replace("ELECTRONICNEWSLETTER_NEWSLETTERDESCRIPTION",news.getDescription());


            // E-posta mesajını oluştur (HTML formatında)
            MimeMessage email = createHtmlEmail(customer.getEmail(), userEmail, subject, htmlContent);

            // E-postayı gönder
            sendMessage(service, "me", email);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    // HTML formatında e-posta oluşturma fonksiyonu
    private MimeMessage createHtmlEmail(String to, String from, String subject, String htmlContent) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        // E-posta gövdesine HTML içeriğini ekle
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

        // Multipart yapı
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        email.setContent(multipart);

        return email;
    }

    // E-postayı Gmail API ile gönderme fonksiyonu
    private void sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {
        Message message = createMessageWithEmail(email);
        service.users().messages().send(userId, message).execute();
    }

    // E-posta mesajını Gmail API'ye uygun formata dönüştürme fonksiyonu
    private Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        Message message = new Message();
        message.setRaw(encodedEmail);

        return message;
    }

    // Kullanıcının token ile gelen e-posta adresini al
    private String getUserEmailFromToken(Gmail service) throws IOException {
        return service.users().getProfile("me").execute().getEmailAddress();
    }
}
