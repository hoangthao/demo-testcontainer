package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductRepository repository;

    @GetMapping
    public Flux<Product> findAll() {
        return Flux.fromIterable(repository.findAll());
    }

    @PostMapping
    public Mono<Product> create(@RequestBody Product request) {
        return Mono.just(repository.save(request));
    }

    @DeleteMapping("/:productId")
    public Mono<Boolean> deleteById(@PathVariable Long productId) {
        return Mono.defer(() -> {
            repository.deleteById(productId);
            return Mono.just(Boolean.TRUE);
        });
    }
}
