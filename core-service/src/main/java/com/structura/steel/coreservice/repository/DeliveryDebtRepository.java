package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.DebtStatus;
import com.structura.steel.coreservice.entity.DeliveryDebt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DeliveryDebtRepository extends JpaRepository<DeliveryDebt, Long> {

	Page<DeliveryDebt> findAllByDeliveryOrderId(Long deliveryId, Pageable pageable);

	List<DeliveryDebt> findAllByDeliveryOrderId(Long deliveryId);

	@Query("SELECT COUNT(d) " +
			"FROM DeliveryDebt d " +
			"WHERE d.deliveryOrder.id = :deliveryOrderId " +
			"AND d.status NOT IN ('PAID', 'CANCELLED')")
	long countNonPaidNonCancelledDebtsByDeliveryOrderId(@Param("deliveryOrderId") Long deliveryOrderId);

	/**
	 * Tìm các khoản nợ vận chuyển theo trạng thái và khoảng thời gian.
	 */
	@Query("SELECT dd FROM DeliveryDebt dd WHERE dd.status IN :statuses AND dd.createdAt BETWEEN :start AND :end")
	List<DeliveryDebt> findByStatusInAndCreatedAtBetween(
			@Param("statuses") List<DebtStatus> statuses,
			@Param("start") Instant start,
			@Param("end") Instant end
	);
}
