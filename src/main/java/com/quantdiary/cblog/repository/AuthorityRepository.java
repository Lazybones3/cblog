package com.quantdiary.cblog.repository;

import com.quantdiary.cblog.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
