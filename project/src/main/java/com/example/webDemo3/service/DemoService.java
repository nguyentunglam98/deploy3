package com.example.webDemo3.service;

import com.example.webDemo3.entity.User;

import java.util.List;

public interface DemoService {
    List<User> findAll();

    User addNew(User user);

    User updateUser(User u);

    void deleteUserByUsername(String username);

    Boolean checkLoginUser(User u);
}
