package com.calendarly.calendarlysvc.repository;

import com.calendarly.calendarlysvc.model.Event;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long groupId);

}
