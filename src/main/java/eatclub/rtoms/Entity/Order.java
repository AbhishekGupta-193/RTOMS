package eatclub.rtoms.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private UUID orderId;

    private String status; // PLACED, PREPARING, DELIVERED

    private double totalAmount;

    private LocalDateTime createdAt = LocalDateTime.now();
}
