package com.calendarly.calendarlysvc.service;

import com.calendarly.calendarlysvc.model.User;
import com.calendarly.calendarlysvc.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getUserById(Integer userId){
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void saveUser(User user) { userRepository.save(user);}

    public User createUser(String firstName, String lastName, String email, String password) {
        // Ensure email doesn't already exist
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Encrypt the password and save to database
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        User user = new User(firstName, lastName, email, encryptedPassword);
        return userRepository.save(user);
    }

}
