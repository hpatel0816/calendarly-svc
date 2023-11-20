package com.calendarly.calendarlysvc.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import com.calendarly.calendarlysvc.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Getter
@Setter
@Table(name="events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(length = 20)
    private String title;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "creator_id")
    private User creator;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    @Column(length = 50)
    private String description;

    @Column(length = 20)
    private String category;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "group_id")
    @JsonIgnoreProperties({"events", "users"})
    private Group group;

    @ElementCollection
    @CollectionTable(name = "event_user_preferences", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "preference")
    private Map<Integer, Status> userPreferences;

    public Event() {}

    public Event(String title, User creator, LocalDate date,
                 LocalTime startTime, LocalTime endTime, String description, String category, Group eventGroup) {
        this.title = title;
        this.creator = creator;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.category = category;
        this.group = eventGroup;
        this.userPreferences = new HashMap<>();
    }

}
