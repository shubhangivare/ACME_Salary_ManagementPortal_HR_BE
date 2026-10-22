package com.peoplepay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peoplepay.api.entity.AuditEvent;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
	List<AuditEvent> findAllByOrderByEventAtDescIdDesc(Pageable pageable);
}
