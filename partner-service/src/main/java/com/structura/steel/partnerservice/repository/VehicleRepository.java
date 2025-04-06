package com.structura.steel.partnerservice.repository;

import com.structura.steel.partnerservice.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByLicensePlate(String licensePlate);

    Page<Vehicle> getAllByPartnerId(Long partnerId, Pageable pageable);

}
