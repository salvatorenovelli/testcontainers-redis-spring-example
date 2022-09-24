# Testcontainers Redis Spring Example

Example on how to create a redis [Testcontainer](https://www.testcontainers.org/) that work with Spring boot without
having to do nasty things with hardcoded ports
or even worse non isolated tests.

Required a bit of digging and testing so hope this will be the end of your copy/paste/modify journey

The logic to initialise the container and inject it in spring is in this
file: [AbstractRedisIntegrationTest.java](src/test/java/com/example/AbstractRedisIntegrationTest.java)

The trick is in using `@DynamicPropertySource` annotation to inject the correct host/port into spring context

```java

@SpringBootTest
public abstract class AbstractRedisIntegrationTest {

    private final static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine"))
                    .withExposedPorts(6379);

    @Autowired
    RedisConnectionFactory connectionFactory;

    @AfterEach
    void dataRedisContainerTestSetup() {
        connectionFactory.getConnection().flushAll();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        redis.start();
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }
}
```
