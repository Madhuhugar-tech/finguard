package com.finguard.controller;

import com.finguard.dto.LoginRequest;
import com.finguard.model.Account;
import com.finguard.model.Role;
import com.finguard.model.User;
import com.finguard.repository.UserRepository;
import com.finguard.service.AccountService;
import com.finguard.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AccountService accountService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AccountService accountService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already registered");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        user.setRole(Role.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Account account = accountService.createAccount(savedUser);

        savedUser.setPassword(null);

        return ResponseEntity.ok(
                Map.of(
                        "user", savedUser,
                        "accountNumber", account.getAccountNumber(),
                        "balance", account.getBalance()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(401)
                    .body("Invalid email or password");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            return ResponseEntity.status(401)
                    .body("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "role", user.getRole().name(),
                        "email", user.getEmail()
                )
        );
    }
}