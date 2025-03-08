# spring-boot-samples
spring-boot-samples


## 默认端口
- zookeeper默认端口：2181
- kafka默认端口：9092

- kafka-0.9.0.0 zookeeper: 12181 kafka: 19092
- kafka-0.11.0.0 zookeeper: 12182 kafka: 19093


## 验证逻辑
1. kafka0.9验证 - 使用spring-kafka::1.0.5.RELEASE
> 前置条件需要排除KafkaAutoConfiguration自动配置
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
    <version>1.0.5.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>0.9.0.0</version>
</dependency>
```
验证结果: 生产者监听器可以正常打印日志

2. kafka0.11验证 - 使用spring-kafka::1.0.5.RELEASE
> 前置条件需要排除KafkaAutoConfiguration自动配置
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
    <version>1.3.11.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>0.11.0.0</version>
</dependency>
```
验证结果: 生产者监听器可以正常打印日志

3. kafka0.11验证 - 使用spring-kafka::2.8.11
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```
验证结果: 生产者监听器可以正常打印日志