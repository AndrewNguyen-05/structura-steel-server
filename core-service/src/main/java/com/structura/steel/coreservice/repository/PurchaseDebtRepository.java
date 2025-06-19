package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.coreservice.entity.PurchaseDebt;
import com.structura.steel.coreservice.entity.analytic.AgingProjection;
import com.structura.steel.coreservice.entity.analytic.DebtStatusDistributionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
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


	/**
	 * Tìm các khoản nợ mua hàng theo trạng thái và khoảng thời gian.
	 */
	@Query("SELECT pd FROM PurchaseDebt pd WHERE pd.status IN :statuses AND pd.createdAt BETWEEN :start AND :end")
	List<PurchaseDebt> findByStatusInAndCreatedAtBetween(
			@Param("statuses") List<DebtStatus> statuses,
			@Param("start") Instant start,
			@Param("end") Instant end
	);

	// ham COALESCE de mac dinh tra ve 0 neu khong co total, bth la se tra ve null
	@Query("SELECT COALESCE(SUM(pd.remainingAmount), 0) FROM PurchaseDebt pd WHERE pd.status IN :statuses")
	java.math.BigDecimal sumRemainingAmountByStatusIn(@Param("statuses") List<DebtStatus> statuses);

	@Query("SELECT pd.status AS status, COUNT(pd) AS count, COALESCE(SUM(pd.remainingAmount), 0) AS totalAmount " +
			"FROM PurchaseDebt pd WHERE pd.status IN :statuses GROUP BY pd.status")
	List<DebtStatusDistributionProjection> getDebtStatusDistribution(@Param("statuses") List<DebtStatus> statuses);

	@Query(value = """
			SELECT po.partner ->> 'partnerName' AS partnerName, pd.remainingAmount AS remainingAmount, pd.createdAt AS createdAt " +
			"FROM PurchaseDebt pd JOIN pd.purchaseOrder po WHERE pd.status IN :statuses
			""", nativeQuery = true)
	List<AgingProjection> getPayableAgingData(@Param("statuses") List<DebtStatus> statuses);
}