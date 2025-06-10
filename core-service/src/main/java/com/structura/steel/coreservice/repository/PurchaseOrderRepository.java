package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

	Page<PurchaseOrder> findByImportCodeContainingIgnoreCase(String importCode, Pageable pageable);

}