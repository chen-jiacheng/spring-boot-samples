package com.chenjiacheng.samples.kafka.config;

import com.chenjiacheng.samples.kafka.kafka.OrderProducerListener;
import com.chenjiacheng.samples.kafka.kafka.ProductProducerListener;
import com.chenjiacheng.samples.kafka.kafka.UserProducerListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenjiacheng on 2025/2/27 01:18
 *
 * @author chenjiacheng
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    private OrderProducerListener orderProducerListener;

    @Autowired
    private ProductProducerListener productProducerListener;

    @Autowired
    private UserProducerListener userProducerListener;

    // 配置 User Topic 的 KafkaTemplate 和 ProducerListener
    @Bean(name = "userProducerFactory")
    public ProducerFactory<String, String> userProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "userKafkaTemplate")
    public KafkaTemplate<String, String> userKafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(userProducerFactory());
        kafkaTemplate.setProducerListener(userProducerListener);
        kafkaTemplate.setDefaultTopic("user");
        return kafkaTemplate;
    }


    // 配置 Order Topic 的 KafkaTemplate 和 ProducerListener
    @Bean(name = "orderProducerFactory")
    public ProducerFactory<String, String> orderProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "orderKafkaTemplate")
    public KafkaTemplate<String, String> orderKafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(orderProducerFactory());
        kafkaTemplate.setProducerListener(orderProducerListener);
        kafkaTemplate.setDefaultTopic("order");
        return kafkaTemplate;
    }

    // 配置 Product Topic 的 KafkaTemplate 和 ProducerListener
    @Bean(name = "productProducerFactory")
    public ProducerFactory<String, String> productProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean(name = "productKafkaTemplate")
    public KafkaTemplate<String, String> productKafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(productProducerFactory());
        kafkaTemplate.setProducerListener(productProducerListener);
        kafkaTemplate.setDefaultTopic("product");
        return kafkaTemplate;
    }

}
