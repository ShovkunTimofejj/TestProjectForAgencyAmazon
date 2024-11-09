//package com.example.testprojectforagencyamazon.configs;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//
//
//@Configuration
//public class RedisConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
//
//    private void logInfo(String message, Object object) {
//        logger.info(message, object);
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        logInfo("Creating RedisTemplate with connection factory: {}", connectionFactory);
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//        logInfo("RedisTemplate created successfully.", template);
//        return template;
//    }
//
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration() {
//        logInfo("Configuring RedisCacheConfiguration with TTL 1 hour.", null);
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
//                        new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
//                        new GenericJackson2JsonRedisSerializer()))
//                .entryTtl(Duration.ofHours(1));
//    }
//
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
//        logInfo("Creating RedisCacheManager with connection factory: {}", connectionFactory);
//        RedisCacheConfiguration defaultCacheConfig = cacheConfiguration();
//        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(defaultCacheConfig)
//                .build();
//        logInfo("RedisCacheManager created successfully.", redisCacheManager);
//        return redisCacheManager;
//    }
//}
