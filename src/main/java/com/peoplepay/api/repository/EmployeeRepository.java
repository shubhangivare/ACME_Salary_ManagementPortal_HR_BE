package com.peoplepay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.peoplepay.api.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
	boolean existsByEmailIgnoreCase(String email);

	java.util.List<Employee> findAllByOrderByNameAsc();

	@Query("select e from Employee e where " +
			"(:search is null or lower(e.name) like lower(concat('%', :search, '%')) " +
			"or lower(e.id) like lower(concat('%', :search, '%')) " +
			"or lower(e.email) like lower(concat('%', :search, '%')) " +
			"or lower(e.department) like lower(concat('%', :search, '%')) " +
			"or lower(e.designation) like lower(concat('%', :search, '%')) " +
			"or lower(e.country) like lower(concat('%', :search, '%'))) " +
			"and (:department is null or e.department = :department) " +
			"and (:country is null or e.country = :country) " +
			"and (:status is null or e.status = :status)")
	Page<Employee> searchPage(@Param("search") String search, @Param("department") String department,
			@Param("country") String country, @Param("status") String status, Pageable pageable);
}

