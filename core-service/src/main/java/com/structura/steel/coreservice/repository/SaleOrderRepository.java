package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.coreservice.entity.SaleOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {

    @Query(value = "SELECT * " +
            "FROM sale_orders " +
            "WHERE (project->>'id')::bigint = :projectId " +
            "AND status = :status " +
            "AND deleted = false", nativeQuery = true)
    List<SaleOrder> findByProjectIdAndStatus(Long projectId, String status);

    Page<SaleOrder> findByExportCodeContainingIgnoreCase(String exportCode, Pageable pageable);
}
