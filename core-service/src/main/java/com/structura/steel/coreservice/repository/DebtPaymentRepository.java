package com.structura.steel.coreservice.repository;

import com.structura.steel.commons.enumeration.DebtType;
import com.structura.steel.coreservice.entity.DebtPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DebtPaymentRepository extends JpaRepository<DebtPayment, Long> {

    @Query("SELECT COALESCE(SUM(dp.amountPaid), 0) FROM DebtPayment dp WHERE dp.paymentDate >= :start AND dp.createdAt < :end AND dp.debtType = :type")
    Optional<BigDecimal> sumAmountPaidByDateAndType(@Param("start") Instant start, @Param("end") Instant end, @Param("type") DebtType type);

    @Query("SELECT COALESCE(SUM(dp.amountPaid), 0) FROM DebtPayment dp WHERE dp.paymentDate >= :start AND dp.createdAt < :end AND dp.debtType IN :types")
    Optional<BigDecimal> sumAmountPaidByDateAndTypes(@Param("start") Instant start, @Param("end") Instant end, @Param("types") List<DebtType> types);

    List<DebtPayment> findByPaymentDateBetween(Instant start, Instant end);

    List<DebtPayment> findByDebtTypeAndDebtIdIn(DebtType debtType, List<Long> debtIds);
}