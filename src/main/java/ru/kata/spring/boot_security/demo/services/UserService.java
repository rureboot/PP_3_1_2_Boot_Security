package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.model.User;

public interface UserService {
    User findByUserName(String userName);
    void Save(User user);

}
