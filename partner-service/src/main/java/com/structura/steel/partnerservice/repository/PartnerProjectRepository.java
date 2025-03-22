package com.structura.steel.partnerservice.repository;

import com.structura.steel.partnerservice.entity.PartnerProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerProjectRepository extends JpaRepository<PartnerProject, Long> {
}
