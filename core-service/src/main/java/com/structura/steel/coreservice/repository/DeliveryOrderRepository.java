package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.coreservice.entity.DeliveryOrder;
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
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

	Page<DeliveryOrder> findByDeletedAndDeliveryCodeContainingIgnoreCase(boolean deleted, String deliveryCode, Pageable pageable);

	Page<DeliveryOrder> findAllByDeleted(boolean deleted, Pageable pageable);

	long countByStatusInAndUpdatedAtBetween(List<OrderStatus> statuses, Instant start, Instant end);

	List<DeliveryOrder> findByStatusInAndUpdatedAtBetween(List<OrderStatus> statuses, Instant start, Instant end);

	List<DeliveryOrder> findByCreatedAtBetween(Instant start, Instant end);

	// ham COALESCE de mac dinh tra ve 0 neu khong co total, bth la se tra ve null
	@Query("SELECT COALESCE(SUM(do.totalDeliveryFee), 0) FROM DeliveryOrder do WHERE do.status = :status AND do.updatedAt BETWEEN :start AND :end AND do.deleted = false")
	BigDecimal sumTotalAmountByStatusAndDateRange(@Param("status") OrderStatus status, @Param("start") Instant start, @Param("end") Instant end);

}
