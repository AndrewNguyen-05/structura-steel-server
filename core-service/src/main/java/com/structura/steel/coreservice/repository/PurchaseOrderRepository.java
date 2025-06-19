package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.OrderStatus;
import com.structura.steel.coreservice.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

	Page<PurchaseOrder> findByDeletedAndImportCodeContainingIgnoreCase(boolean deleted, String importCode, Pageable pageable);

	Page<PurchaseOrder> findAllByDeleted(boolean deleted, Pageable pageable);

	// Thêm các phương thức khác nếu cần cho báo cáo hàng ngày
	long countByCreatedAtBetween(java.time.Instant start, java.time.Instant end);
	List<PurchaseOrder> findByCreatedAtBetween(java.time.Instant start, java.time.Instant end);

	@Query(value = """
   SELECT * FROM purchase_orders po 
   WHERE 
       (po.project->>'id')::bigint = :projectId
       AND po.created_at >= :afterTime 
       AND po.status = :status
       AND po.deleted = false
   """, nativeQuery = true)
	List<PurchaseOrder> findByProjectIdAndCreatedAtAfterAndStatus(
			@Param("projectId") Long projectId,
			@Param("afterTime") Instant afterTime,
			@Param("status") String status
	);

	// ham COALESCE de mac dinh tra ve 0 neu khong co total, bth la se tra ve null
	@Query("SELECT COALESCE(SUM(po.totalAmount), 0) FROM PurchaseOrder po WHERE po.status = :status AND po.updatedAt BETWEEN :start AND :end AND po.deleted = false")
	java.math.BigDecimal sumTotalAmountByStatusAndDateRange(@Param("status") OrderStatus status, @Param("start") Instant start, @Param("end") Instant end);

}