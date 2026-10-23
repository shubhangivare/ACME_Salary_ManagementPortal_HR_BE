package com.peoplepay.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.peoplepay.api.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	@Query("select u from AppUser u join fetch u.role where lower(u.username)=lower(:username)")
	Optional<AppUser> findByUsernameWithRole(@Param("username") String username);

	boolean existsByUsernameIgnoreCase(String username);
}
