package com.quantdiary.cblog.service;

import com.quantdiary.cblog.domain.Authority;

import java.util.Optional;

public interface AuthorityService {
    Optional<Authority> getAuthorityById(Long id);
}
