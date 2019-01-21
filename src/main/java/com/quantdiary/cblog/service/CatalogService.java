package com.quantdiary.cblog.service;

import com.quantdiary.cblog.domain.Catalog;
import com.quantdiary.cblog.domain.User;

import java.util.List;
import java.util.Optional;

public interface CatalogService {

    //保存分类
    Catalog saveCatalog(Catalog catalog);

    //删除分类
    void removeCatalog(Long id);

    //根据id获取分类
    Optional<Catalog> getCatalogById(Long id);

    //获取Catalog列表
    List<Catalog> listCatalogsByUser(User user);

    List<Catalog> listCatalogs();
}
