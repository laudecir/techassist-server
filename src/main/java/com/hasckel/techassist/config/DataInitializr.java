package com.hasckel.techassist.config;

import com.hasckel.techassist.model.Role;
import com.hasckel.techassist.model.User;
import com.hasckel.techassist.repository.RoleRepository;
import com.hasckel.techassist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializr implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        List<User> all = userRepository.findAll();
        if (all.isEmpty()) {
            this.createNewUser("Laudecir Hasckel", "laudecir@techassist.com", "123456", Role.RoleType.ADMIN);
            this.createNewUser("João das Neves", "joao@techassist.com", "123456", Role.RoleType.TECHNICAL);
            this.createNewUser("José Silvestre", "jose@techassist.com", "123456", Role.RoleType.TECHNICAL);
            this.createNewUser("Maria das Dores", "maria@techassist.com", "123456", Role.RoleType.RECEPCIONIST);
        }
    }

    private void createNewUser(String name, String username, String password, Role.RoleType roleType) {
        Role role = new Role();
        role.setType(roleType);
        roleRepository.save(role);

        User user = new User(name, username, passwordEncoder.encode(password), Arrays.asList(role));
        userRepository.save(user);
    }

}
