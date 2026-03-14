package com.example.bai4.config;

import com.example.bai4.model.Account;
import com.example.bai4.model.Role;
import com.example.bai4.repository.AccountRepository;
import com.example.bai4.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Tạo Role nếu chưa có
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_ADMIN");
                    return roleRepository.save(r);
                });

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName("ROLE_USER");
                    return roleRepository.save(r);
                });

        // Tạo Account admin nếu chưa có
        if (accountRepository.findByLoginName("admin").isEmpty()) {
            Account admin = new Account();
            admin.setLoginName("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(roleAdmin));
            accountRepository.save(admin);
        }

        // Tạo Account user1 nếu chưa có
        if (accountRepository.findByLoginName("user1").isEmpty()) {
            Account user1 = new Account();
            user1.setLoginName("user1");
            user1.setPassword(passwordEncoder.encode("123456"));
            user1.setRoles(Set.of(roleUser));
            accountRepository.save(user1);
        }
    }
}
