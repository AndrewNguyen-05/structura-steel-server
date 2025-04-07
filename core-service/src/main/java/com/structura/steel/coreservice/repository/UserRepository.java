package com.structura.steel.coreservice.repository;

import com.structura.steel.coreservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
