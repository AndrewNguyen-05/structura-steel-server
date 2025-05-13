package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.SaleOrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOrderDetailRepository extends JpaRepository<SaleOrderDetail, Long> {
	Page<SaleOrderDetail> findAllBySaleOrderId(Long saleId, Pageable pageable);
}
