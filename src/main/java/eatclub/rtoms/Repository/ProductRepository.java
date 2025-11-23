package eatclub.rtoms.Repository;

import eatclub.rtoms.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> { }
