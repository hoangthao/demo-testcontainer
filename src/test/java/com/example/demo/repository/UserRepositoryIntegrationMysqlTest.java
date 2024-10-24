package com.example.demo.repository;

import com.example.demo.config.TestConfig;
import com.example.demo.entity.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
@Import(TestConfig.class)
@TestPropertySource(locations="classpath:test.properties")
@EnabledIf(expression = "#{environment.acceptsProfiles('tc')}", loadContext = true)
class UserRepositoryIntegrationMysqlTest {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:5.7.34")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProductRepository repository;

    @Test
    @Transactional
    @Sql(scripts = "classpath:insert-products.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:delete-products.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testSaveUser() {
        Product product = new Product();
        product.setCode("JD");
        product.setProductName("John Doe");
        product.setPrice(11.5);
        repository.save(product);

        Product savedProduct = repository.findById(product.getId()).orElse(null);
        Assertions.assertNotNull(savedProduct);
        Assertions.assertEquals("John Doe", savedProduct.getProductName());

        Assertions.assertEquals(3, repository.count());
    }
}