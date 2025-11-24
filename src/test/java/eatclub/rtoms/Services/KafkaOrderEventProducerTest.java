package eatclub.rtoms.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KafkaOrderEventProducerTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private KafkaOrderEventProducer producer;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        producer = new KafkaOrderEventProducer(kafkaTemplate);
    }

    @Test
    void testConstructorInjection() {
        assertNotNull(producer, "Producer should not be null");
    }

    @Test
    void testSendStatusUpdateEvent_Success() {
        // Arrange
        String message = "ORDER_PLACED";

        // Act
        producer.sendStatusUpdateEvent(message);

        // Assert
        verify(kafkaTemplate, times(1))
                .send(eq("order-status-topic"), eq(message));
    }

    @Test
    void testSendStatusUpdateEvent_NullMessage() {
        // Act
        producer.sendStatusUpdateEvent(null);

        // Assert
        verify(kafkaTemplate, times(1))
                .send(eq("order-status-topic"), isNull());
    }

    @Test
    void testSendStatusUpdateEvent_EmptyMessage() {
        // Arrange
        String empty = "";

        // Act
        producer.sendStatusUpdateEvent(empty);

        // Assert
        verify(kafkaTemplate, times(1))
                .send(eq("order-status-topic"), eq(empty));
    }

    @Test
    void testSendStatusUpdateEvent_MessageCaptured() {
        // Arrange
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        String msg = "STATUS_UPDATED";

        // Act
        producer.sendStatusUpdateEvent(msg);

        // Assert
        verify(kafkaTemplate).send(eq("order-status-topic"), captor.capture());
        assertEquals(msg, captor.getValue());
    }
}
