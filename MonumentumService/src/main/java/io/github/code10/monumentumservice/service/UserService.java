package io.github.code10.monumentumservice.service;

import io.github.code10.monumentumservice.controller.exception.ForbiddenException;
import io.github.code10.monumentumservice.controller.exception.NotFoundException;
import io.github.code10.monumentumservice.model.domain.Settings;
import io.github.code10.monumentumservice.model.domain.User;
import io.github.code10.monumentumservice.model.dto.SettingsDto;
import io.github.code10.monumentumservice.repository.UserRepository;
import io.github.code10.monumentumservice.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User findCurrentUser() {
        final UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ForbiddenException("User is not authenticated!");
        }

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findByEmail(userDetails.getUsername());
    }

    public User create(String name, String email, String password) {
        return userRepository.save(new User(name, email, passwordEncoder.encode(password), Constants.USER_ROLE, new Settings()));
    }

    public void saveSettings(User user, SettingsDto request) {
        final Settings settings = user.getSettings();
        settings.setKeepLoggedIn(request.getKeepLoggedIn());
        settings.setDistanceUnit(request.getDistanceUnit());
        user.setSettings(settings);

        userRepository.save(user);
    }
}
