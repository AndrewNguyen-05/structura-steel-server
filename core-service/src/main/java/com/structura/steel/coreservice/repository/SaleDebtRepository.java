package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.coreservice.entity.SaleDebt;
import com.structura.steel.coreservice.entity.analytic.AgingProjection;
import com.structura.steel.coreservice.entity.analytic.DebtStatusDistributionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public interface SaleDebtRepository extends JpaRepository<SaleDebt, Long> {

	Page<SaleDebt> findAllBySaleOrderId(Long saleId, Pageable pageable);

	List<SaleDebt> findAllBySaleOrderId(Long saleId);

	@Query("SELECT COUNT(sd) " +
			"FROM SaleDebt sd " +
			"WHERE sd.saleOrder.id = :saleOrderId " +
			"AND sd.status NOT IN ('PAID', 'CANCELLED')")
	long countNonPaidNonCancelledDebtsBySaleOrderId(@Param("saleOrderId") Long saleOrderId);

	/**
	 * Tìm các khoản nợ bán hàng theo danh sách trạng thái và trong khoảng thời gian.
	 * @param statuses Danh sách các trạng thái cần tìm (ví dụ: UNPAID, PARTIALLY_PAID).
	 * @param start Thời gian bắt đầu.
	 * @param end Thời gian kết thúc.
	 * @return Danh sách các khoản nợ thỏa mãn điều kiện.
	 */
	@Query("SELECT sd FROM SaleDebt sd WHERE sd.status IN :statuses AND sd.createdAt BETWEEN :start AND :end")
	List<SaleDebt> findByStatusInAndCreatedAtBetween(
			@Param("statuses") List<DebtStatus> statuses,
			@Param("start") Instant start,
			@Param("end") Instant end
	);

	// ham COALESCE de mac dinh tra ve 0 neu khong co total, bth la se tra ve null
	@Query("SELECT COALESCE(SUM(sd.remainingAmount), 0) FROM SaleDebt sd WHERE sd.status IN :statuses")
	BigDecimal sumRemainingAmountByStatusIn(@Param("statuses") List<DebtStatus> statuses);

	@Query("SELECT sd.status AS status, COUNT(sd) AS count, COALESCE(SUM(sd.remainingAmount), 0) AS totalAmount " +
			"FROM SaleDebt sd WHERE sd.status IN :statuses GROUP BY sd.status")
	List<DebtStatusDistributionProjection> getDebtStatusDistribution(@Param("statuses") List<DebtStatus> statuses);

	@Query(value = """
			SELECT so.partner ->> 'partnerName' AS partnerName, sd.remainingAmount AS remainingAmount, sd.createdAt AS createdAt " +
			"FROM SaleDebt sd JOIN sd.saleOrder so WHERE sd.status IN :statuses
			""", nativeQuery = true)
	List<AgingProjection> getReceivableAgingData(List<DebtStatus> statuses);

	List<SaleDebt> findBySaleOrderId(Long saleOrderId);
}

