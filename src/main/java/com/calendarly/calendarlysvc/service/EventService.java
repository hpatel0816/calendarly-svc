package com.calendarly.calendarlysvc.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.calendarly.calendarlysvc.model.Event;
import com.calendarly.calendarlysvc.model.Group;
import com.calendarly.calendarlysvc.model.enums.Status;
import com.calendarly.calendarlysvc.model.User;
import com.calendarly.calendarlysvc.repository.EventRepository;

@Service
public class EventService {
    
    private final EventRepository eventRepository;
    private final UserService userService;
    private final GroupService groupService;
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    public EventService(EventRepository eventRepository, UserService userService, GroupService groupService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.groupService = groupService;
    }

    public Event getEventById(Long eventId){
        return eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("The event (ID: " + eventId + ") doesn't exist."));
    }

    public Event createEvent(String title, Integer creatorId, String date,
                            String startTime, String endTime, 
                            String description, String category, Status status, Integer groupId) {

        logger.info("hit createEvent endpoint.");
        // Ensure the user and group exist *** This can be absracted away further like userExists && groupExists
        User eventCreator = userService.getUserById(creatorId);
        logger.info("Retrieved user " + eventCreator);
        Group eventGroup = groupService.getGroupById(groupId);
        logger.info("Retrieved group " + eventGroup);

        if (eventGroup.getUsers().stream().anyMatch(user -> user.getUserId().equals(creatorId))) {
            //If user is in the group, then create event
            LocalDate eventDate = LocalDate.parse(date);
            LocalTime eventStartTime = LocalTime.parse(startTime);
            LocalTime eventEndTime = LocalTime.parse(endTime);

            Event event = new Event(title, eventCreator, eventDate, eventStartTime, eventEndTime, description, category, eventGroup);
            logger.info("Created event " + event);
            event.getUserPreferences().put(creatorId, Status.YES);
            return eventRepository.save(event);
        } else {
            throw new IllegalArgumentException("Unable to create the event. The user is a not in the group. ");
        }
    }

    public void updateTime(Long eventId, String timeType, String time) {
        LocalTime newTime = LocalTime.parse(time);
        Event event = getEventById(eventId);
        if (timeType == "start-time") {
            event.setStartTime(newTime);
        } else {
            event.setEndTime(newTime);
        }
    }

    public void updateDescription(Long eventId, String description) {
        Event event = getEventById(eventId);
        event.setDescription(description);
    }

    public void updateDate(Long eventId, String date) {
        LocalDate newDate = LocalDate.parse(date);
        Event event = getEventById(eventId);
        event.setDate(newDate);
    }

    public void updateTitle(Long eventId, String title) {
        Event event = getEventById(eventId);
            event.setTitle(title);
    }

    public void updateUserPreferenece(Long eventId, Integer userId, String preference) {
        Status newPreference = Status.valueOf(preference.toUpperCase());
        Event event = getEventById(eventId);
        User user = userService.getUserById(userId);
        if (event.getUserPreferences().containsKey(user.getUserId())) {
            event.getUserPreferences().put(user.getUserId(), newPreference);
        } else {
            logger.warn("Could not find the user with ID" + userId);
        }
    }

    public void deleteEvent(Long eventId) {
        Event event = getEventById(eventId);
        //Remove the event from its group before deleting it
        Group group = event.getGroup();
        if (group != null) {
            group.getEvents().remove(event);
            groupService.updateGroup(group);
        } else {
            logger.info("Unable to find the associated group for the event to delete from.");
        }
        eventRepository.deleteById(eventId);
    }
}
