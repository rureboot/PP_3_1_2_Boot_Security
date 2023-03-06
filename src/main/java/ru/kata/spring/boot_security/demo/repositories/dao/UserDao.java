package ru.kata.spring.boot_security.demo.repositories.dao;

import ru.kata.spring.boot_security.demo.model.User;


public interface UserDao {
    User getInitializedUser(String username);
}
