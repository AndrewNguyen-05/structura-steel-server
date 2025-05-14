package com.structura.steel.partnerservice.repository;

import com.structura.steel.partnerservice.entity.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
	Page<Partner> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String nameKeyword, String codeKeyword, Pageable pageable);
}
