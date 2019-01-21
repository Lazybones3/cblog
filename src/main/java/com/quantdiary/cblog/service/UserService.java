package com.quantdiary.cblog.service;

import com.quantdiary.cblog.domain.User;

public interface UserService {

    User saveUser(User user);

    User updateUser(User user);

    User getUserById(Long id);
}
