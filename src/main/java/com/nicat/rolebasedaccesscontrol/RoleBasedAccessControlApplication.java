package com.nicat.rolebasedaccesscontrol;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RoleBasedAccessControlApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoleBasedAccessControlApplication.class, args);
    }
}