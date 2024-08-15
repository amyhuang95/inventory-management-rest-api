package example.inventory_management_rest_api.config;

import java.time.Duration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    // Configure Spring Boot Redis Lettuce
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);

        return new LettuceConnectionFactory(configuration);
    }

    // Default Redis cache manager
    //  @Bean
    //  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    //    return RedisCacheManager.create(connectionFactory);
    //  }

    // Add RedisCacheManager to use Redis as a backing implementation and customize Redis Cache Configuration
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration("inventory", myDefaultCacheConfig(Duration.ofMinutes(5)))
                .withCacheConfiguration("inventoryItem", myDefaultCacheConfig(Duration.ofMinutes(1)))
                .build();
    }

    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    // Check Redis connection
    @PostConstruct
    public void logRedisConfig() {
        System.out.println("Redis Host: " + redisHost);
        System.out.println("Redis Port: " + redisPort);
    }

}