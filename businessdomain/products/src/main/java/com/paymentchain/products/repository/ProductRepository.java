package com.paymentchain.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentchain.products.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
