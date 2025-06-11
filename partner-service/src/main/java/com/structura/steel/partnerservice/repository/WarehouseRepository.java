package com.structura.steel.partnerservice.repository;

import com.structura.steel.partnerservice.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Page<Warehouse> findAllByPartnerIdAndDeleted(Long partnerId, boolean deleted, Pageable pageable);

    Optional<Warehouse> findByIdAndPartnerIdAndDeleted(Long id, Long partnerId, boolean deleted);

    Optional<Warehouse> findByIdAndPartnerId(Long id, Long partnerId);

    List<Warehouse> findAllByDeletedTrueAndUpdatedAtBefore(Instant cutoffDate);
}
