package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.PurchaseDebt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDebtRepository extends JpaRepository<PurchaseDebt, Long> {
	Page<PurchaseDebt> findAllByPurchaseOrderId(Long purchaseId, Pageable pageable);
}