package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    User findByUserName(String userName);

    void save(User user);
    List<User> findAll();
    void deleteById(Long id);

    List<Role> findAllRoles();
    User findById(Long id);

}
