package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.PurchaseOrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Long> {
	Page<PurchaseOrderDetail> findAllByPurchaseOrderId(Long purchaseOrderId, Pageable pageable);
}