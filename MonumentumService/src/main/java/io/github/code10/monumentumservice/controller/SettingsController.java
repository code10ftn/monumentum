package io.github.code10.monumentumservice.controller;

import io.github.code10.monumentumservice.model.domain.User;
import io.github.code10.monumentumservice.model.dto.SettingsDto;
import io.github.code10.monumentumservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/settings")
public class SettingsController {

    private final UserService userService;

    @Autowired
    public SettingsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity findSettings() {
        final User currentUser = userService.findCurrentUser();
        return new ResponseEntity<>(new SettingsDto(currentUser.getSettings()), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity updateSettings(@RequestBody SettingsDto request) {
        final User currentUser = userService.findCurrentUser();
        userService.saveSettings(currentUser, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
