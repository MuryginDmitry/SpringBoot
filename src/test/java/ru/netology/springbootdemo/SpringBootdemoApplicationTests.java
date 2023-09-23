package ru.netology.springbootdemo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    @Container
    private static final GenericContainer<?> devContainer = new GenericContainer<>("devapp")
            .withExposedPorts(8080);

    @Container
    private static final GenericContainer<?> prodContainer = new GenericContainer<>("prodapp")
            .withExposedPorts(8081);
    @BeforeAll
    public static void setUp() {
        devContainer.start();
        prodContainer.start();
    }
    @Test
    void testDevContainer() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "http://localhost:" + devContainer.getMappedPort(8080)+"/profile", String.class);

        assertEquals("Current profile is dev", responseEntity.getBody());
        System.out.println(responseEntity.getBody());
    }

    @Test
    void testProdContainer() {
         ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                "http://localhost:" + prodContainer.getMappedPort(8081)+"/profile", String.class);

        assertEquals("Current profile is production", responseEntity.getBody());
        System.out.println(responseEntity.getBody());
    }
}