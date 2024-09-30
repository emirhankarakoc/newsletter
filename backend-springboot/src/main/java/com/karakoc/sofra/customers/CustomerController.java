package com.karakoc.sofra.customers;

import com.karakoc.sofra.exceptions.general.BadRequestException;
import com.karakoc.sofra.exceptions.general.NotfoundException;
import com.karakoc.sofra.newsletters.Newsletter;
import com.karakoc.sofra.newsletters.NewsletterRepository;
import com.karakoc.sofra.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    private final NewsletterRepository newsletterRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    @PostMapping("/newsletters/{newsletterId}")
    public ResponseEntity subscribeToNewsletter(@PathVariable String newsletterId, @RequestBody CreateCustomerRequest r){
        log.info("yeni biri kaydoldu.");
       return customerService.subscribeToNewsletter(newsletterId, r);
    }


    @GetMapping("/newsletters/{newsletterId}")
    public List<Customer> getAllCustomers(@AuthenticationPrincipal UserPrincipal principal, @PathVariable String newsletterId){
        return customerService.getAllCustomers(principal.getUserId(),newsletterId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@AuthenticationPrincipal UserPrincipal principal,@PathVariable String id) {
        return customerService.deleteCustomer(principal.getUserId(),id);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@AuthenticationPrincipal UserPrincipal principal, @PathVariable String id, @RequestBody UpdateCustomerRequest r){
        return customerService.updateCustomer(principal.getUserId(),id,r);
    }
}
