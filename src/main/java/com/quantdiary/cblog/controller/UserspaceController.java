package com.quantdiary.cblog.controller;

import com.quantdiary.cblog.domain.Blog;
import com.quantdiary.cblog.domain.Catalog;
import com.quantdiary.cblog.domain.Status;
import com.quantdiary.cblog.domain.User;
import com.quantdiary.cblog.service.BlogService;
import com.quantdiary.cblog.service.CatalogService;
import com.quantdiary.cblog.service.UserService;
import com.quantdiary.cblog.util.ConstraintViolationExceptionHandler;
import com.quantdiary.cblog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;


    //获取个人编辑页面
    @GetMapping("/{username}/profile")
    public ModelAndView profile(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("profile", "userModel", model);
    }

    //保存个人设置
    @PostMapping("/{username}/profile")
    public String saveProfile(@PathVariable("username") String username, User user) {
        User originalUser = userService.getUserById(user.getId());

        //判断密码是否变更
        String rawPassword = originalUser.getPassword();
        //加密前端获取的密码
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword, encodePassword);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());
        }
        userService.saveUser(originalUser);
        return "redirect:/u/" + username + "/profile";
    }

    //获取博客展示页面
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username,
                              @PathVariable("id") Long id, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogsByUser(user);

        Blog blog = null;
        Optional<Blog> optionalBlog = blogService.getBlogById(id);
        if (optionalBlog.isPresent()) {
            blog = optionalBlog.get();
        }

        model.addAttribute("blogModel", blog);
        model.addAttribute("catalogs", catalogs);

        return "blogview";
    }

    //获取写博客页面
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogsByUser(user);
        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("catalogs", catalogs);
        return new ModelAndView("blogedit", "blogModel", model);
    }

    //保存博客
    @PostMapping("/{username}/blogs/edit")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
        // 对 Catalog 进行空处理
        if (blog.getCatalog().getId() == null) {
            return ResponseEntity.ok().body(new Response(false,"未选择分类"));
        }
        try {
            if (blog.getId() != null) {
                Optional<Blog> optionalBlog = blogService.getBlogById(blog.getId());
                if (optionalBlog.isPresent()) {
                    Blog orignalBlog = optionalBlog.get();
                    orignalBlog.setTitle(blog.getTitle());
                    orignalBlog.setContent(blog.getContent());
                    orignalBlog.setHtmlContent(blog.getHtmlContent());
                    orignalBlog.setCatalog(blog.getCatalog());
                    blogService.saveBlog(orignalBlog);
                }
            } else {
                User user = (User) userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

    // 博客管理页面
    @GetMapping("/{username}/blogs")
    public String blogs(@PathVariable("username") String username,
                        @RequestParam(value = "async", required = false) boolean async,
                        @RequestParam(value = "status", defaultValue = "PUBLIC") String statusName,
                        @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                        Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);

        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        Status status = Status.valueOf(statusName);
        Page<Blog> page = blogService.listBlogsByUserAndStatus(user, status, pageable);

        List<Blog> list = page.getContent();
        int totalPages = page.getTotalPages();

        List<Catalog> catalogs = catalogService.listCatalogsByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("blogList", list);
        model.addAttribute("catalogs", catalogs);
        model.addAttribute("page", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("status", statusName);
        return (async ? "bloglist :: #mainContainerRepleace" : "bloglist");
    }

    // 获取编辑博客的界面
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username") String username,
                                 @PathVariable("id") Long id,
                                 Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogsByUser(user);

        Blog blog = null;
        Optional<Blog> optionalBlog = blogService.getBlogById(id);
        if (optionalBlog.isPresent()) {
            blog = optionalBlog.get();
        }
        model.addAttribute("blog", blog);
        model.addAttribute("catalogs", catalogs);
        return new ModelAndView("blogedit", "blogModel", model);
    }

    // 删除博客
    @DeleteMapping("/{username}/blogs/delete/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,
                                               @PathVariable("id") Long id,
                                               @RequestParam(value = "status", defaultValue = "PUBLIC") String statusName) {
        try {
            if (Status.valueOf(statusName).equals(Status.DELETED)) {
                blogService.deleteBlog(id);
            } else {
                blogService.updateBlogStatus(id, Status.DELETED);
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "博客删除成功", null));
    }

    //修改状态
    @PostMapping("/{username}/blogs/status/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> changeBlogStatus(@PathVariable("username") String username,
                                                     @PathVariable("id") Long id,
                                                     @RequestParam(value = "status", defaultValue = "PUBLIC") String statusName) {

        try {
            Status status = Status.valueOf(statusName);
            if (status.equals(Status.PUBLIC)) {
                    blogService.updateBlogStatus(id, Status.SECRET);
            } else {
                blogService.updateBlogStatus(id, Status.PUBLIC);
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "设置成功", null));
    }

}
