package com.example;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class ItalianDictionaryTest extends DataRedisContainerTest {

    @Autowired
    ItalianDictionary sut;

    @BeforeEach
    void setUp() {
        sut.save("Hello!", "Ciao!");
        sut.save("Test containers could be simpler in spring!", "Usare i testcontainers in Spring e' un dito in culo");
    }

    @Test
    void shouldFindMultiple() {
        assertThat(sut.findAll(), Matchers.containsInAnyOrder(
                "Ciao!",
                "Usare i testcontainers in Spring e' un dito in culo"
        ));
    }

    @Test
    void shouldFind() {
        assertThat(sut.findById("Hello!").get(), is("Ciao!"));
    }

    @Test
    void shouldReturnEmptyIdNoMatch() {
        assertFalse(sut.findById("Good night").isPresent());
    }
}