package com.peoplepay.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.peoplepay.api.entity.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	Optional<AppRole> findByName(String name);
}
