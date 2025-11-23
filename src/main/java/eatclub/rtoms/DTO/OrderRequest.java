package eatclub.rtoms.DTO;

import java.util.List;
import java.util.UUID;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private UUID customerId;
    private UUID restaurantId;
    private List<OrderItemRequest> items;
}
