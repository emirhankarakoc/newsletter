package com.karakoc.sofra.customers;

import com.karakoc.sofra.exceptions.general.BadRequestException;
import com.karakoc.sofra.exceptions.general.ForbiddenException;
import com.karakoc.sofra.exceptions.general.NotfoundException;
import com.karakoc.sofra.newsletters.Newsletter;
import com.karakoc.sofra.newsletters.NewsletterRepository;
import com.karakoc.sofra.user.User;
import com.karakoc.sofra.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor

public class CustomerManager implements CustomerService{
    private final CustomerRepository customerRepository;
    private final NewsletterRepository newsletterRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity subscribeToNewsletter(String newsletterId, CreateCustomerRequest r) {
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
        return ResponseEntity.ok("Joined successfully.");    }

    @Override
    public List<Customer> getAllCustomers(String userId, String newsletterId) {

        Newsletter news = newsletterRepository.findById(newsletterId).orElseThrow(()-> new NotfoundException("Newsletter not found."));
        User owner = userRepository.findById(userId).orElseThrow(()->new NotfoundException("User not found."));
        if (news.getOwnerUserId().equals(owner.getId())){
            return news.getCustomers();
        }
        else{
            throw new ForbiddenException("Forbidden.");
        }
    }

    @Override
    public ResponseEntity deleteCustomer(String newsletterOwnerId, String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new NotfoundException("Customer not found."));
        User owner = userRepository.findById(newsletterOwnerId).orElseThrow(()-> new NotfoundException("User not found."));
        Newsletter newsletter = newsletterRepository.findById(customer.getNewsletterId()).orElseThrow(()-> new NotfoundException("Newsletter not found."));
        if (newsletter.getOwnerUserId().equals(owner.getId())){
            newsletter.getCustomers().remove(customer);
            newsletterRepository.save(newsletter);
            return ResponseEntity.ok("Deleted successfully.");
        }
        else {
            throw new ForbiddenException("Forbidden.");
        }
    }

    @Override
    public Customer updateCustomer(String newsletterOwnerId, String id, UpdateCustomerRequest r) {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new NotfoundException("Customer not found."));
        Optional<Customer> registeredUserCheck = customerRepository.findByEmailAndNewsletterId(r.getEmail(),id);
        if (registeredUserCheck.isPresent()){
            throw new BadRequestException("This email already registered.");
        }

        User owner = userRepository.findById(newsletterOwnerId).orElseThrow(()-> new NotfoundException("User not found."));
        Newsletter newsletter = newsletterRepository.findById(customer.getNewsletterId()).orElseThrow(()-> new NotfoundException("Newsletter not found."));
        if (newsletter.getOwnerUserId().equals(owner.getId())){
            customer.setName(r.getName());
            customer.setEmail(r.getEmail());
            return customerRepository.save(customer);
        }
        else {
            throw new ForbiddenException("Forbidden.");
        }





    }

}
