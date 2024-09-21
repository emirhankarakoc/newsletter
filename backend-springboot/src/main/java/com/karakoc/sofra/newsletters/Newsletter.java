package com.karakoc.sofra.newsletters;


import com.karakoc.sofra.cloudinary.entity.Image;
import com.karakoc.sofra.customers.Customer;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Newsletter {
    @Id
    private String id;
    private String name;
    private String ownerUserId;
    private String description;
    private String imageUrl;
    private String imageId;
    @OneToMany
    @JoinColumn(name = "customerId")
    private List<Customer> customers;

}
