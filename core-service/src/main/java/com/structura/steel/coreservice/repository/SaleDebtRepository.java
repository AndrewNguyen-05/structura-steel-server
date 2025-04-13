package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.SaleDebt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleDebtRepository extends JpaRepository<SaleDebt, Long> {
}

