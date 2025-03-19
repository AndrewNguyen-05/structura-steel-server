package com.structura.steel.productservice.repository;

import com.structura.steel.productservice.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
}
