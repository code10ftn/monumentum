package io.github.code10.monumentumservice.controller;

import io.github.code10.monumentumservice.model.domain.User;
import io.github.code10.monumentumservice.model.dto.auth.AuthenticationRequest;
import io.github.code10.monumentumservice.model.dto.auth.AuthenticationResponse;
import io.github.code10.monumentumservice.model.dto.auth.SignUpRequest;
import io.github.code10.monumentumservice.security.TokenUtils;
import io.github.code10.monumentumservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    private final TokenUtils tokenUtils;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserService userService, TokenUtils tokenUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping
    public ResponseEntity authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final User user = userService.findByEmail(userDetails.getUsername());
        final String token = tokenUtils.generateToken(userDetails);
        return new ResponseEntity<>(new AuthenticationResponse(user.getId(), token), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        final User user = userService.create(signUpRequest.getName(), signUpRequest.getEmail(), signUpRequest.getPassword());
        return new ResponseEntity<>(user.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/sign-out")
    public ResponseEntity signout() {
        SecurityContextHolder.clearContext();
        return new ResponseEntity(HttpStatus.OK);
    }
}
