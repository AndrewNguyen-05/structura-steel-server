package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.coreservice.entity.DeliveryOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

	Page<DeliveryOrder> findByDeletedAndDeliveryCodeContainingIgnoreCase(boolean deleted, String deliveryCode, Pageable pageable);

	Page<DeliveryOrder> findAllByDeleted(boolean deleted, Pageable pageable);

	long countByStatusAndUpdatedAtBetween(OrderStatus status, Instant start, Instant end);

	List<DeliveryOrder> findByStatusAndUpdatedAtBetween(OrderStatus status, Instant start, Instant end);

	List<DeliveryOrder> findByCreatedAtBetween(Instant start, Instant end);
}
