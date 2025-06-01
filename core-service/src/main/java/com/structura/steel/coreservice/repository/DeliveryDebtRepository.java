package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.DeliveryDebt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
