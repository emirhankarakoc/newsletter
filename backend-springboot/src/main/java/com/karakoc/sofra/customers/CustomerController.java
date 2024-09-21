package com.karakoc.sofra.customers;

import com.karakoc.sofra.exceptions.general.BadRequestException;
import com.karakoc.sofra.exceptions.general.NotfoundException;
import com.karakoc.sofra.newsletters.Newsletter;
import com.karakoc.sofra.newsletters.NewsletterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    private final NewsletterRepository newsletterRepository;
    private final CustomerRepository customerRepository;

    @PostMapping("/newsletters/{newsletterId}")
    public ResponseEntity subscribeToNewsletter(@PathVariable String newsletterId, @RequestBody CreateCustomerRequest r){
        log.info("yeni biri kaydoldu.");
        //niye servis yazayim ki? bence hallolur.
        Newsletter newsletter = newsletterRepository.findById(newsletterId).orElseThrow(()-> new NotfoundException("Newsletter not found."));
        Optional<Customer> registeredUserCheck = customerRepository.findByEmailAndNewsletterId(r.getEmail(),newsletterId);
        if (registeredUserCheck.isPresent()){
            throw new BadRequestException("This email already registered.");
        }
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setName(r.getName());
        customer.setEmail(r.getEmail());
        customer.setNewsletterId(newsletterId);
        customerRepository.save(customer);
        newsletter.getCustomers().add(customer);
        newsletterRepository.save(newsletter);
        return ResponseEntity.ok("Joined successfully.");
    }
}
