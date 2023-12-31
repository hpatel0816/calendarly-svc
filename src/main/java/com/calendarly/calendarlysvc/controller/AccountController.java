package com.calendarly.calendarlysvc.controller;

import com.calendarly.calendarlysvc.dto.requests.SignUpRequest;
import com.calendarly.calendarlysvc.dto.responses.UserLoginResponse;
import com.calendarly.calendarlysvc.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.calendarly.calendarlysvc.service.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    public AccountController(AccountService accountService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.accountService = accountService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody SignUpRequest signUpData) {
        try {
            User newUser = accountService.createUser(signUpData.getFirstName(), signUpData.getLastName(), signUpData.getEmail(), signUpData.getPassword());
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
            User existingUser = accountService.getUserByEmail(email);
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

    @PatchMapping("/change-email")
    public ResponseEntity<String> changeUserEmail(@RequestParam("oldEmail") String oldEmail, @RequestParam("newEmail") String newEmail) {
        try {
            accountService.changeEmail(oldEmail, newEmail);
            logger.info("Modified the user's email to " + newEmail);
            return ResponseEntity.status(HttpStatus.OK).body("Email changed successfully.");
        } catch (IllegalArgumentException e) {
            logger.warn("Unable to find the user with the " + oldEmail + " email.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with the older email doesn't exist.");
        } catch (Exception e) {
            logger.error("An error occured while updating the user's email.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update the user's email.");
        }
    }

    @PatchMapping("/change-password")
    public ResponseEntity<String> changeUserEmail(@RequestParam("userId") Integer userId, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        try {
            accountService.changePassword(userId, oldPassword, newPassword);
            logger.info("Modified the user's password to " + newPassword);
            return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully.");
        } catch (IllegalArgumentException e) {
            logger.warn("Unable to find the user with the " + userId + " user ID.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("An account with the provided user ID doesn't exist.");
        } catch (Exception e) {
            logger.error("An error occured while updating the user's password.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update the user's password.");
        }
    }
}
