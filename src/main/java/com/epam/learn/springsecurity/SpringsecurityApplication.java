package com.epam.learn.springsecurity;

import com.epam.learn.springsecurity.reository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SpringsecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecurityApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository repository) {
        return args -> {
            var users = repository.findAll();
            log.info("Users:");
            log.info("----------------------------------");
            users.forEach(user -> log.info("Name %s Password %s".formatted(user.getName(), user.getPassword())));
            log.info("");
        };
    }
}
