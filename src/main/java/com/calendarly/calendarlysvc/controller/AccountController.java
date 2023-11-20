package com.calendarly.calendarlysvc.controller;

import com.calendarly.calendarlysvc.dto.requests.SignUpRequest;
import com.calendarly.calendarlysvc.dto.responses.UserLoginResponse;
import com.calendarly.calendarlysvc.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.calendarly.calendarlysvc.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    public AccountController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody SignUpRequest signUpData) {
        try {
            User newUser = userService.createUser(signUpData.getFirstName(), signUpData.getLastName(), signUpData.getEmail(), signUpData.getPassword());
            logger.info("User created. (ID: " + newUser.getUserId() + ")");
            return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully!");
        } catch (IllegalArgumentException e) {
            logger.warn("The email (" + signUpData.getEmail() + ") is already used by another account");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with that email already exists.");
        } catch (Exception e) {
            logger.error("An error occured while creating the account.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to create account.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        try {
            // Retrieve the user from the database
            User existingUser = userService.getUserByEmail(email);
            // Compare the entered password with the stored hashed password
            if (bCryptPasswordEncoder.matches(password, existingUser.getPassword())) {
                logger.info("User logged in successfully.");
                UserLoginResponse loginResponse = new UserLoginResponse(existingUser.getUserId(), "Login successful!");
                return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
            } else {
                logger.info("The provided password doesn't match the user's password.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password.");
            }
        } catch (IllegalArgumentException e) {
            logger.warn("User account with " + email + " email doesn't exist.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sorry, that account doesn't exist.");
        } catch (Exception e) {
            logger.warn("Unable to find user account associated with " + email + " email.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to find user.");
        }
    }
}
