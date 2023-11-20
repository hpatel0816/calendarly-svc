package com.calendarly.calendarlysvc.controller;

import com.calendarly.calendarlysvc.dto.requests.CreateEventRequest;
import com.calendarly.calendarlysvc.dto.responses.EventCreatedResponse;
import com.calendarly.calendarlysvc.model.Event;
import com.calendarly.calendarlysvc.model.enums.Status;
import com.calendarly.calendarlysvc.service.EventService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/get-event")
    public ResponseEntity<?> getEvent(@RequestParam("eventId") Long eventId) {
        try {
            Event event = eventService.getEventById(eventId);
            logger.info("Found event with ID " + eventId);
            return ResponseEntity.status(HttpStatus.OK).body(event);
        } catch (IllegalArgumentException e) {
            logger.info("Event with ID " + eventId + "doesn't exist.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e);
        } catch (Exception e) {
            logger.warn("Unable to find the event with " + eventId + " ID.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to retrieve the event.");
        }
        
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody CreateEventRequest event) {
        try {
            Event newEvent = eventService.createEvent(event.getTitle(), event.getCreator(), event.getDate(), 
                            event.getStartTime(), event.getEndTime(), event.getDescription(), 
                            event.getCategory(), Status.YES, event.getGroupId());
            logger.info("Created event with ID " + newEvent.getEventId());
            EventCreatedResponse createdResponse = new EventCreatedResponse(newEvent, "Event created successfully!");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdResponse);
        } catch (Exception e) {
            logger.error("An error occured while creating the event.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to create event.");
        }
    }

    @PutMapping("/update-time")
    public ResponseEntity<String> updateStartTime(@RequestParam("eventId") Long eventId, @RequestParam("time") String updatedTime, @RequestParam("timeType") String timeType) {
        try {
            eventService.updateTime(eventId, timeType, updatedTime);
            logger.info("Changed event " + timeType + " to " + updatedTime);
            return ResponseEntity.status(HttpStatus.OK).body("Event start time updated.");
        } catch (Exception e) {
            logger.error("An error occured while updating the event time.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update event time.");
        }
    }

    @PutMapping("/update-description")
    public ResponseEntity<String> updateDescription(@RequestParam("eventId") Long eventId, @RequestParam("description") String updatedDescription) {
        try {
            eventService.updateDescription(eventId, updatedDescription);
            logger.info("Changed event description to " + updatedDescription);
            return ResponseEntity.status(HttpStatus.OK).body("Event description updated.");
        } catch (Exception e) {
            logger.error("An error occured while updating the event description.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update event description.");
        }
    }

    @PutMapping("/update-date")
    public ResponseEntity<String> updateDate(@RequestParam("eventId") Long eventId, @RequestParam("date") String updatedDate) {
        try {
            eventService.updateDate(eventId, updatedDate);
            logger.info("Changed event date to " + updatedDate);
            return ResponseEntity.status(HttpStatus.OK).body("Event description updated.");
        } catch (Exception e) {
            logger.error("An error occured while updating the event date.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update event date.");
        }
    }

    @PutMapping("/update-title")
    public ResponseEntity<String> updateTitle(@RequestParam("eventId") Long eventId, @RequestParam("title") String updatedTitle) {
        try {
            eventService.updateTitle(eventId, updatedTitle);
            logger.info("Changed event title to " + updatedTitle);
            return ResponseEntity.status(HttpStatus.OK).body("Event description updated.");
        } catch (Exception e) {
            logger.error("An error occured while updating the event title.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update event title.");
        }
    }

    @PutMapping("/update-user-preference")
    public ResponseEntity<String> updateUserPreference(@RequestParam("eventId") Long eventId, @RequestParam("userId") Integer userId, @RequestParam("preference") String updatedPreference) {
        try {
            eventService.updateUserPreferenece(eventId, userId, updatedPreference);
            logger.info("Changed the user preference to " + updatedPreference);
            return ResponseEntity.status(HttpStatus.OK).body("Event description updated.");
        } catch (Exception e) {
            logger.error("An error occured while updating the user's preference for the event.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to update the user preference.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEvent(@RequestParam("eventId") Long eventId) {
        try {
            eventService.deleteEvent(eventId);
            logger.info("The event (ID:" + eventId + ") was deleted.");
            return ResponseEntity.status(HttpStatus.OK).body("Event deleted successfully.");
        } catch (Exception e) {
            logger.error("An error occured while deleting the event.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to delete event.");
        }
    }








    



}
