package com.quantdiary.cblog.repository;

import com.quantdiary.cblog.domain.Catalog;
import com.quantdiary.cblog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    //根据用户和分类名字查询
    List<Catalog> findByUserAndName(User user, String name);

    //根据用户查询
    List<Catalog> findByUser(User user);
}
