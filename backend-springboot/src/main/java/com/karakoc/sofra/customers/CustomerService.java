package com.karakoc.sofra.customers;

import com.karakoc.sofra.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CustomerService {

    ResponseEntity subscribeToNewsletter(String newsletterId, CreateCustomerRequest r);
    List<Customer> getAllCustomers(String userId, String newsletterId);
    ResponseEntity deleteCustomer(String newsletterOwnerId, String id);
    Customer updateCustomer(String newsletterOwnerId, String id, UpdateCustomerRequest r);
}
