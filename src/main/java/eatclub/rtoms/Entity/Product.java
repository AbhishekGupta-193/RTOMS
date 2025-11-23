package eatclub.rtoms.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue
    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String name;
    private double price;

    private LocalDateTime createdAt = LocalDateTime.now();
}

