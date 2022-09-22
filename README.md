# Testcontainers Redis Spring Example

Example on how to create a redis testcontainer that work with Spring boot without having to do nasty things with hardcoded ports
or even worse non isolated tests.

Required a bit of digging and testing so hope this will help the end of your copy/paste/modify journey


The logic to initialise the container and inject it in spring is in this file: [DataRedisContainerTest.java](src/test/java/com/example/DataRedisContainerTest.java)


The trick is in using `ApplicationContextInitializer<ConfigurableApplicationContext>` to inject the correct host/port into spring context

    static class RedisContainerContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            GenericContainer<?> redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(REDIS_PORT);
            redis.start();

            log.info("Redis server is available at {}:{}", redis.getHost(), redis.getMappedPort(REDIS_PORT));

            TestPropertyValues values = TestPropertyValues.of(
                    "spring.redis.host=" + redis.getHost(),
                    "spring.redis.port=" + redis.getMappedPort(REDIS_PORT)
            );
            values.applyTo(applicationContext);
        }

    }

