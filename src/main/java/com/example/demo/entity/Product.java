package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@ToString
@EqualsAndHashCode
public class Product {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO) // create sequence product_seq start with 1 increment by 50
    @GeneratedValue(strategy = GenerationType.IDENTITY) // not use seq
    private Long id;

    @Column
    private String code;
    @Column
    private String productName;
    @Column
    private double price;
}
