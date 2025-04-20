package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.DeliveryDebt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryDebtRepository extends JpaRepository<DeliveryDebt, Long> {
}
