package com.epam.learn.springsecurity.service;

import com.epam.learn.springsecurity.handler.CustomAuthenticationFailureHandler;
import com.epam.learn.springsecurity.model.AuthoritiesEntity;
import com.epam.learn.springsecurity.reository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException(CustomAuthenticationFailureHandler.USER_IS_BLOCKED_MSG);
        }

        var authorities = user.getAuthorities().stream()
                .map(AuthoritiesEntity::getAuthority)
                .toArray(String[]::new);
        return User.withUsername(user.getName())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
