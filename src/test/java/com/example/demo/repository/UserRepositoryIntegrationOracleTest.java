package com.example.demo.repository;

import com.example.demo.config.TestConfig;
import com.example.demo.entity.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;

import javax.sql.DataSource;


@SpringBootTest
@Testcontainers
@Import(TestConfig.class)
@TestPropertySource(locations="classpath:test.properties")
class UserRepositoryIntegrationOracleTest {

    @Container
    private static final OracleContainer oracleContainer = new OracleContainer("gvenzl/oracle-free:23.4-slim-faststart")
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

    @TestConfiguration
    static class InternalConfig {
        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url(oracleContainer.getJdbcUrl())
                    .username(oracleContainer.getUsername())
                    .password(oracleContainer.getPassword())
                    .build();
        }
    }
}