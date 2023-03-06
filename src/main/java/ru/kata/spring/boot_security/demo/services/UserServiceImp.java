package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.repositories.dao.UserDao;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImp implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;


    private final UserDao userDao;



    @Autowired

    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder encoder, UserDao userDao) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.userDao = userDao;
        initAdmin();
    }



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = getInitializedUserByUsername(username);
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


            String encryptedPassword = encoder.encode(user.getPassword());
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


    private User getInitializedUserByUsername(String username) {

        User user = userDao.getInitializedUser(username);
        return user;
    }




    @Transactional
    protected void initAdmin() {
        User userAdmin = userRepository.findByUsername("admin");

        if (userAdmin == null) {

            userAdmin = new User();
            userAdmin.setUsername("admin");
            userAdmin.setEmail("admin@admin");

            userAdmin.setPassword(encoder.encode("admin"));

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

            Set<Role> roleSet = new HashSet<>();
            roleSet.add(roleAdmin);
            roleSet.add(roleUser);
            userAdmin.setRoles(roleSet);
            userRepository.save(userAdmin);


        }
    }
}
