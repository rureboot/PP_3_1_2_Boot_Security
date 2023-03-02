package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.PersistenceContext;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserServiceImp implements UserDetailsService, UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;


    @Autowired

    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        initAdmin();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found in DB");
        }
        return user;
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public void Save(User user) {
        roleRepository.findById(1L);
    }


    @Transactional
    protected void initAdmin() {
        User userAdmin = userRepository.findByUsername("admin");

        if (userAdmin == null) {

            userAdmin = new User();
            userAdmin.setUsername("admin");
            userAdmin.setEmail("admin@admin");
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userAdmin.setPassword(bCryptPasswordEncoder.encode("admin"));

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            if (roleAdmin == null) {

                roleAdmin = new Role();
                roleAdmin.setName("ROLE_ADMIN");
                roleRepository.save(roleAdmin);
            }

            userAdmin.setRoles(Arrays.asList(roleAdmin));
            userRepository.save(userAdmin);


        }
    }
}
