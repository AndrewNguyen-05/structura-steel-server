package com.structura.steel.productservice.repository;

import com.structura.steel.productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByDeletedFalseAndNameContainingIgnoreCaseOrDeletedFalseAndCodeContainingIgnoreCase(String nameKeyword, String codeKeyword, Pageable pageable);

    Optional<Product> findByIdAndDeletedFalse(Long id);

    List<Product> findAllByIdInAndDeletedFalse(List<Long> ids);
}