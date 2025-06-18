package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.SaleOrderDetail;
import com.structura.steel.coreservice.entity.analytic.TopProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SaleOrderDetailRepository extends JpaRepository<SaleOrderDetail, Long> {

	Page<SaleOrderDetail> findAllBySaleOrderId(Long saleId, Pageable pageable);

	List<SaleOrderDetail> findAllBySaleOrderId(Long saleId);

	@Query(value = """
			SELECT sod.product ->> 'name' AS name, SUM(sod.subtotal) AS value
			FROM sale_order_details sod
			JOIN sale_orders so ON sod.id_sale_order = so.id
			WHERE so.created_at BETWEEN :start AND :end AND so.deleted = false
			GROUP BY sod.product ->> 'name'
			ORDER BY value DESC
			LIMIT :limit
		""", nativeQuery = true)
	List<TopProductProjection> findTopProducts(@Param("start") Instant start, @Param("end") Instant end, @Param("limit") int limit);

}
