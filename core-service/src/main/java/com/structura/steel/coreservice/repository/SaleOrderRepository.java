package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, String> {
}
