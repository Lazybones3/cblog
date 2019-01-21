package com.quantdiary.cblog.controller;

import com.quantdiary.cblog.domain.Blog;
import com.quantdiary.cblog.domain.Catalog;
import com.quantdiary.cblog.service.BlogService;
import com.quantdiary.cblog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

    @GetMapping
    public String listBlogs(@RequestParam(value = "async", required = false) boolean async,
                            @RequestParam(value = "catalog", required = false) Long catalogId,
                            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                            Model model) {
        Page<Blog> page = null;
        List<Blog> list = null;
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        if (catalogId != null && catalogId > 0) {
            Optional<Catalog> optionalCatalog = catalogService.getCatalogById(catalogId);
            Catalog catalog = null;
            if (optionalCatalog.isPresent()) {
                catalog = optionalCatalog.get();
                page = blogService.listBlogsByCatalog(catalog, pageable);
            }
        } else {
            page = blogService.listBlogs(pageable);
        }

        list = page.getContent();
        int totalPages = page.getTotalPages();

        List<Catalog> catalogs = catalogService.listCatalogs();
        Set<Catalog> catalogsSet = new HashSet<>(catalogs);


        model.addAttribute("blogList", list);
        model.addAttribute("catalogs", catalogsSet);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        return (async ? "index :: #mainContainerRepleace" : "index");
    }
}
