package com.calendarly.calendarlysvc.repository;

import com.calendarly.calendarlysvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(Integer userId);
    Optional<User> findByEmail(String email);
}
