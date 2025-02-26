package com.chenjiacheng.samples.kafka.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductProducerListener implements ProducerListener<String, String> {

    @Override
    public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
        log.info("Product message sent successfully: {}", producerRecord.value());
    }

    @Override
    public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("Failed to send product message: {}", producerRecord.value(), exception);
    }

}