package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@SpringBootTest
@TestPropertySource(locations="classpath:test.properties")
@EnabledIf(expression = "#{environment.acceptsProfiles('tc')}", loadContext = true)
public class TestWithProfile {

    @Test
    void test1() {
        System.out.println("hello");
    }
}
