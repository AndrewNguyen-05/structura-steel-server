package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.coreservice.entity.SaleDebt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}

