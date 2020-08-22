package com.example.webDemo3.service.impl;

import com.example.webDemo3.entity.User;
import com.example.webDemo3.repository.UserRepository;
import com.example.webDemo3.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User addNew(User user) {
        if(user.getUsername() != null) {
            return userRepository.save(user);
        }
        return user;
    }

    @Override
    public User updateUser(User u) {
        User oldUser = userRepository.findUserByUsername(u.getUsername());
        if (oldUser != null) {
            oldUser.setPassword(u.getPassword());
            oldUser.setName(u.getName());
            oldUser = userRepository.save(oldUser);
        }
        return oldUser;
    }

    @Override
    public void deleteUserByUsername(String username) {
        userRepository.deleteById(username);
    }

    /**
     *
     * @param u
     * @return
     */
    @Override
    public Boolean checkLoginUser(User u) {
        User user = userRepository.findUserByUsername(u.getUsername());
        if(user!=null && u.getPassword().equals(user.getPassword())){
            return true;
        }
        return false;
    }
}
