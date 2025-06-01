package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.SaleDebt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleDebtRepository extends JpaRepository<SaleDebt, Long> {

	Page<SaleDebt> findAllBySaleOrderId(Long saleId, Pageable pageable);

	List<SaleDebt> findAllBySaleOrderId(Long saleId);

	@Query("SELECT COUNT(sd) " +
			"FROM SaleDebt sd " +
			"WHERE sd.saleOrder.id = :saleOrderId " +
			"AND sd.status NOT IN ('PAID', 'CANCELLED')")
	long countNonPaidNonCancelledDebtsBySaleOrderId(@Param("saleOrderId") Long saleOrderId);

}

