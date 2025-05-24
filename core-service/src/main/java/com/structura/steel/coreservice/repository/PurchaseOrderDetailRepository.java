package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.PurchaseOrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Long> {

	Page<PurchaseOrderDetail> findAllByPurchaseOrderId(Long purchaseOrderId, Pageable pageable);

	List<PurchaseOrderDetail> findAllByPurchaseOrderId(Long purchaseOrderId);
}