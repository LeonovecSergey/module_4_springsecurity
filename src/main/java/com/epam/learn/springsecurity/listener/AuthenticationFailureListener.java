package com.epam.learn.springsecurity.listener;

import com.epam.learn.springsecurity.reository.UserRepository;
import com.epam.learn.springsecurity.service.LoginAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService loginAttemptService;
    private final UserRepository userRepository;
    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        var principal = event.getAuthentication().getPrincipal();

        if (principal instanceof String username) {
            if (userRepository.findByName(username).isPresent()){
                loginAttemptService.loginFailed(username);
            }
        }

    }
}
