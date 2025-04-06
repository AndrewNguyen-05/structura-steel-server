package com.structura.steel.partnerservice.repository;

import com.structura.steel.partnerservice.entity.PartnerProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PartnerProjectRepository extends JpaRepository<PartnerProject, Long> {
    Page<PartnerProject> getAllByPartnerId(Long partnerId, Pageable pageable);
}
