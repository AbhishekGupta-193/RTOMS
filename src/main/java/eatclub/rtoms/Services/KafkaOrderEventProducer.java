package eatclub.rtoms.Services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaOrderEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendStatusUpdateEvent(String message) {
        kafkaTemplate.send("order-status-topic", message);
    }
}
