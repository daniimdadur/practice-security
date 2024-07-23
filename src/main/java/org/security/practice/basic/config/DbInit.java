package org.security.practice.basic.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.security.practice.basic.auth.model.RoleEntity;
import org.security.practice.basic.auth.model.UserEntity;
import org.security.practice.basic.auth.repository.RoleRepo;
import org.security.practice.basic.auth.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DbInit implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public void run(String... args) throws Exception {
        initRole();
        initUser();
    }

    private void initRole() {
        if (this.roleRepo.count() > 0) {
            log.info("Role has been initialized");
            return;
        }

        try {
            var roleList = Arrays.asList(
                    new RoleEntity("ROLE_USER"),
                    new RoleEntity("ROLE_ADMIN"),
                    new RoleEntity("ROLE_SUPER_USER")
            );
            this.roleRepo.saveAllAndFlush(roleList);
            log.info("Role has been initialized");
        } catch (Exception e) {
            log.error("Save role failed : {}", e.getMessage());
        }
    }

    public void initUser() {
        if (this.userRepo.count() > 0) {
            log.info("User has been initialized");
            return;
        }
        List<UserEntity> userEntityList = new ArrayList<>();

        RoleEntity roleUser = this.roleRepo.findByName("ROLE_USER").orElse(null);
        if (roleUser != null) {
            userEntityList.add(new UserEntity("user", "satu", "user01@gmail.com", this.passwordEncoder.encode("Us3r1234"), Arrays.asList(roleUser)));
        }

        RoleEntity roleAdmin = this.roleRepo.findByName("ROLE_ADMIN").orElse(null);
        if (roleAdmin != null) {
            userEntityList.add(new UserEntity("admin", "satu", "admin01@gmail.com", this.passwordEncoder.encode("P@ssW0rd32!"), Arrays.asList(roleAdmin)));
        }

        RoleEntity roleSuperUser = this.roleRepo.findByName("ROLE_SUPER_USER").orElse(null);
        if (roleSuperUser != null) {
            userEntityList.add(new UserEntity("super", "user", "super.user@gmail.com", this.passwordEncoder.encode("D@n!1mdadu1213"), Arrays.asList(roleSuperUser)));
        }

        try {
            this.userRepo.saveAllAndFlush(userEntityList);
            log.info("User has been initialized");
        }catch (Exception e) {
            log.error("Save user failed: {}", e.getMessage());
        }
    }
}
