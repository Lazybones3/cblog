package com.quantdiary.cblog.service;

import com.quantdiary.cblog.domain.Blog;
import com.quantdiary.cblog.domain.Status;
import com.quantdiary.cblog.domain.User;
import com.quantdiary.cblog.repository.BlogRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogServiceImplTest {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testListBlogsByStatus() {
        Status blog_status = Status.PUBLIC;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Blog> page = blogRepository.findByStatus(blog_status, pageable);
        List<Blog> list = page.getContent();
        for (Blog blog :
                list) {
            System.out.println(blog.getTitle());
        }
        System.out.println(page.getTotalElements());
    }

    @Test
    public void testUpdateBlogStatus() {
        Long blog_id = 2L;
        Status blog_status = Status.DELETED;
        blogRepository.updateStatusById(blog_id, blog_status);
    }

    @Test
    public void testListBlogsByUserAndStatus() {

        User user = userService.getUserById(1L);
        Status status = Status.SECRET;
        Pageable pageable = PageRequest.of(0,10);
        Page<Blog> page = blogRepository.findByUserAndStatus(user, status, pageable);
        for (Blog blog: page.getContent()) {
            System.out.println(blog.getTitle());
        }
    }

}
