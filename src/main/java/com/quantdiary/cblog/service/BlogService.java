package com.quantdiary.cblog.service;

import com.quantdiary.cblog.domain.Blog;
import com.quantdiary.cblog.domain.Catalog;
import com.quantdiary.cblog.domain.Status;
import com.quantdiary.cblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BlogService {

    Blog saveBlog(Blog blog);

    void deleteBlog(Long id);

    Optional<Blog> getBlogById(Long id);

    Page<Blog> listBlogs(Pageable pageable);

    Page<Blog> listBlogsByUser(User user, Pageable pageable);

    Page<Blog> listBlogsByUserAndStatus(User user, Status status, Pageable pageable);

    Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable);

    void updateBlogStatus(Long id, Status status);
}
