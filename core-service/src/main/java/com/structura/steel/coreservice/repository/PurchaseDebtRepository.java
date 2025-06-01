package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.PurchaseDebt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDebtRepository extends JpaRepository<PurchaseDebt, Long> {

	Page<PurchaseDebt> findAllByPurchaseOrderId(Long purchaseId, Pageable pageable);

	List<PurchaseDebt> findAllByPurchaseOrderId(Long purchaseId);

	// Đếm các khoản nợ mua chưa được giải quyết (không phải PAID và không phải CANCELLED)
	@Query("SELECT COUNT(pd) " +
			"FROM PurchaseDebt pd " +
			"WHERE pd.purchaseOrder.id = :purchaseOrderId " +
			"AND pd.status NOT IN ('PAID', 'CANCELLED')")
	long countNonPaidNonCancelledDebtsByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);

}