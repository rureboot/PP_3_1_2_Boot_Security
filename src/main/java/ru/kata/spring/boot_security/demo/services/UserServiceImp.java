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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


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
    public void save(User user) {

        for (String roleName : user.getListPossibleRoles()) {
            Role role = roleRepository.findByName(roleName);
            user.addRole(role);
        }
        Long userId;
        if (( userId = user.getId()) == null || !user.getPassword().equals("leave old password")) {

            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }else {
            Optional<User> oldUserData = userRepository.findById(userId);
            user.setPassword(oldUserData.get().getPassword());
        }

        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {

        userRepository.deleteById(id);
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
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
            Role roleUser = roleRepository.findByName("ROLE_USER");
            if (roleUser == null) {
                roleUser = new Role();
                roleUser.setName("ROLE_USER");
                roleRepository.save(roleUser);
            }

            userAdmin.setRoles(Arrays.asList(roleAdmin, roleUser));
            userRepository.save(userAdmin);


        }
    }
}
