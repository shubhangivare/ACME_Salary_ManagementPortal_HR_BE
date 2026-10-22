package com.peoplepay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.peoplepay.api.entity.AuditEvent;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {
	List<AuditEvent> findAllByOrderByEventAtDescIdDesc(Pageable pageable);
}
