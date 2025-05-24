package com.structura.steel.partnerservice.repository;

import com.structura.steel.partnerservice.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByLicensePlate(String licensePlate);

    // Check if a non-deleted vehicle with the license plate exists
    boolean existsByLicensePlateAndDeletedFalse(String licensePlate);

    // Check if a non-deleted vehicle with the license plate exists (excluding a specific ID)
    boolean existsByLicensePlateAndDeletedFalseAndIdNot(String licensePlate, Long id);

    // Get all vehicles by partnerId, filtering by deleted status
    Page<Vehicle> findAllByPartnerIdAndDeleted(Long partnerId, boolean deleted, Pageable pageable);

    // Find a vehicle by ID and Partner ID, filtering by deleted status
    Optional<Vehicle> findByIdAndPartnerIdAndDeleted(Long id, Long partnerId, boolean deleted);

    // Find any vehicle by ID and Partner ID (including deleted)
    Optional<Vehicle> findByIdAndPartnerId(Long id, Long partnerId);

    // Find any vehicle by ID (including deleted) - needed for restore/soft-delete
    Optional<Vehicle> findById(Long id);

}
