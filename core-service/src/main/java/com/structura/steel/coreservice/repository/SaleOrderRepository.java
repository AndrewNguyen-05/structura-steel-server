package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.coreservice.entity.SaleOrder;
import com.structura.steel.coreservice.entity.analytic.RevenueOverTimeProjection;
import com.structura.steel.coreservice.entity.analytic.TopCustomerProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
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

    @Query(value = """
            SELECT TO_CHAR(date_trunc('day', so.created_at), 'DD-MM-YYYY') AS dateLabel, SUM(so.total_amount) AS totalRevenue
            FROM sale_orders so
            WHERE so.created_at BETWEEN :start AND :end AND so.deleted = false
            GROUP BY date_trunc('day', so.created_at)
            ORDER BY date_trunc('day', so.created_at) ASC
        """, nativeQuery = true)
    List<RevenueOverTimeProjection> findRevenueOverTime(@Param("start") Instant start, @Param("end") Instant end);

    @Query(value = """
            SELECT so.partner ->> 'partnerName' AS name, SUM(so.total_amount) AS value
            FROM sale_orders so
            WHERE so.created_at BETWEEN :start AND :end AND so.deleted = false
            GROUP BY so.partner ->> 'partnerName'
            ORDER BY value DESC
            LIMIT :limit
        """, nativeQuery = true)
    List<TopCustomerProjection> findTopCustomers(@Param("start") Instant start, @Param("end") Instant end, @Param("limit") int limit);

    // ham COALESCE de mac dinh tra ve 0 neu khong co total, bth la se tra ve null
    @Query("SELECT COALESCE(SUM(so.totalAmount), 0) FROM SaleOrder so WHERE so.status = :status AND so.updatedAt BETWEEN :start AND :end AND so.deleted = false")
    java.math.BigDecimal sumTotalAmountByStatusAndDateRange(@Param("status") OrderStatus status, @Param("start") Instant start, @Param("end") Instant end);
}
