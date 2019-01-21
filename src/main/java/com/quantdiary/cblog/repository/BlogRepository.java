package com.quantdiary.cblog.repository;

import com.quantdiary.cblog.domain.Blog;
import com.quantdiary.cblog.domain.Catalog;
import com.quantdiary.cblog.domain.Status;
import com.quantdiary.cblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByUser(User user, Pageable pageable);

    Page<Blog> findByUserAndStatus(User user, Status status, Pageable pageable);

    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);

    Page<Blog> findByStatus(Status status, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Blog b SET b.status = :status WHERE b.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") Status status);
}
