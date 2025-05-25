package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.DebtPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtPaymentRepository extends JpaRepository<DebtPayment, Long> {
}