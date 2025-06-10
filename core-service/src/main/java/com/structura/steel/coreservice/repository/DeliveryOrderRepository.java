package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.DeliveryOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

	Page<DeliveryOrder> findByDeletedAndDeliveryCodeContainingIgnoreCase(boolean deleted, String deliveryCode, Pageable pageable);

	Page<DeliveryOrder> findAllByDeleted(boolean deleted, Pageable pageable);
}
