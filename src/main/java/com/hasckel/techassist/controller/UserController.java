package com.hasckel.techassist.controller;

import com.hasckel.techassist.annotation.CurrentUser;
import com.hasckel.techassist.exception.ResourceNotFoundException;
import com.hasckel.techassist.model.Role;
import com.hasckel.techassist.model.User;
import com.hasckel.techassist.repository.RoleRepository;
import com.hasckel.techassist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    @Secured({"ROLE_ADMIN"})
    public Page<User> all(@RequestParam(value = "page", defaultValue = "0") int page,
                          @RequestParam(value = "size", defaultValue = "15") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");

        return userRepository.findAll(pageable);
    }

    @PutMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<User> edit(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userRepository.save(user);

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<User> get(@PathVariable Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return ResponseEntity.ok(user);
    }

    @GetMapping("/roles")
    public List<Role> roles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(@CurrentUser final User user) {
        return ResponseEntity.ok(user);
    }

}
