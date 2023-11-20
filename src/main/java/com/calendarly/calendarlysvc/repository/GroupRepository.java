package com.calendarly.calendarlysvc.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.calendarly.calendarlysvc.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findAll();
    Optional<Group> findById(Integer groupId);
    Optional<Group> findByGroupCode(UUID groupCode);
}
