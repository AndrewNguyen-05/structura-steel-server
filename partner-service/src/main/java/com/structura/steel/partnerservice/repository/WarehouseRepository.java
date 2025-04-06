package com.structura.steel.partnerservice.repository;

import com.structura.steel.partnerservice.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Page<Warehouse> getAllByPartnerId(Long partnerId, Pageable pageable);
}
