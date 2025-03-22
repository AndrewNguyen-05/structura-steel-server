package com.structura.steel.productservice.repository;

import com.structura.steel.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);
    List<Product> findByNameContainingIgnoreCase(String name);
    boolean existsByCode(String code);
    boolean existsById(Long id);
}