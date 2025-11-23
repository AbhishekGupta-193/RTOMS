package eatclub.rtoms.Repository;

import eatclub.rtoms.Entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Inventory findByProduct_ProductId(UUID productId);
}


