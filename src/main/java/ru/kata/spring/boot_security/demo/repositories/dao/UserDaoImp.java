package ru.kata.spring.boot_security.demo.repositories.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User getInitializedUser(String username) {
        String hqlQuery = "select u from User u " +
                "join fetch u.roles " +
                "where u.username=:username ";
        User user = entityManager.createQuery(hqlQuery, User.class)
                .setParameter("username", username)
                .getSingleResult();
        return user;
    }
}
