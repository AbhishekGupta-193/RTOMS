package eatclub.rtoms.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private UUID productId;

    private String name;
    private double price;

    private LocalDateTime createdAt = LocalDateTime.now();
}

