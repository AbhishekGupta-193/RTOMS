package eatclub.rtoms.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    private UUID customerId;

    private String name;
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;
}
