package com.quantdiary.cblog.service;

import com.quantdiary.cblog.domain.Blog;
import com.quantdiary.cblog.domain.Catalog;
import com.quantdiary.cblog.domain.Status;
import com.quantdiary.cblog.domain.User;
import com.quantdiary.cblog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    @Override
    public Page<Blog> listBlogs(Pageable pageable) {
        Status status = Status.PUBLIC;
        return blogRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Blog> listBlogsByUser(User user, Pageable pageable) {
        return blogRepository.findByUser(user, pageable);
    }

    @Override
    public Page<Blog> listBlogsByUserAndStatus(User user, Status status, Pageable pageable) {
        return blogRepository.findByUserAndStatus(user, status, pageable);
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        return blogRepository.findByCatalog(catalog, pageable);
    }

    @Override
    public void updateBlogStatus(Long id, Status status) {
        blogRepository.updateStatusById(id, status);
    }
}
