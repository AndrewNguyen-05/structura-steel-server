package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.coreservice.entity.SaleOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {

    @Query(value = "SELECT * " +
            "FROM sale_orders " +
            "WHERE (project->>'id')::bigint = :projectId " +
            "AND status = :status " +
            "AND deleted = false", nativeQuery = true)
    List<SaleOrder> findByProjectIdAndStatus(Long projectId, String status);

    Page<SaleOrder> findByDeletedAndExportCodeContainingIgnoreCase(boolean deleted, String exportCode, Pageable pageable);

    Page<SaleOrder> findAllByDeleted(boolean deleted, Pageable pageable);

    @Query("SELECT so FROM SaleOrder so WHERE so.status IN :statuses AND so.updatedAt BETWEEN :start AND :end")
    List<SaleOrder> findByStatusInAndUpdatedAtBetween(
            @Param("statuses") List<OrderStatus> statuses,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    long countByCreatedAtBetween(Instant start, Instant end);

    @Query("SELECT SUM(so.totalAmount) FROM SaleOrder so WHERE so.createdAt BETWEEN :start AND :end")
    Optional<BigDecimal> sumTotalAmountByCreatedAtBetween(@Param("start") Instant start, @Param("end") Instant end);

    List<SaleOrder> findByCreatedAtBetween(Instant start, Instant end);

}
