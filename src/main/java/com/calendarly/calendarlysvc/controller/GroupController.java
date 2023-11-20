package com.calendarly.calendarlysvc.controller;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.calendarly.calendarlysvc.dto.responses.GroupCreatedResponse;
import com.calendarly.calendarlysvc.model.Event;
import com.calendarly.calendarlysvc.model.Group;
import com.calendarly.calendarlysvc.model.helpers.GroupInfo;
import com.calendarly.calendarlysvc.service.GroupService;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestParam("groupName") String groupName, @RequestParam("groupAdmin") Integer groupAdminId) {
        try {
            Group newGroup = groupService.createGroup(groupName, groupAdminId);
            logger.info("Created group with ID " + newGroup.getGroupId());
            GroupCreatedResponse createdResponse = new GroupCreatedResponse(newGroup.getGroupId(), newGroup.getGroupCode().toString(),"Login successful!");
            return ResponseEntity.status(HttpStatus.CREATED).body(createdResponse);
        } catch (Exception e) {
            logger.error("An error occured while creating the group.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to create group.");
        }
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinGroup(@RequestParam("groupCode") String groupCode, @RequestParam("userId") Integer userId) {
        try {
            logger.info("Group code: " + groupCode);
            groupService.addGroupMember(groupCode, userId);
            logger.info("The user was added to the group successfully.");
            return ResponseEntity.status(HttpStatus.OK).body("User was added to the group!");
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occured while adding user to the group.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to join the group.");
        }
    }

    @GetMapping("/get-groups")
    public ResponseEntity<?> getAllGroups(@RequestParam("userId") Integer userId) {
        try {
            Set<GroupInfo> groups = groupService.getAll(userId);
            logger.info("Found the user's groups.");
            return ResponseEntity.status(HttpStatus.OK).body(groups);
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occured while getting all the user's groups.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get the user's groups.");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getGroupEvents(@RequestParam("groupId") Integer groupId) {
        try {
            Set<Event> events = groupService.getGroupEvents(groupId);
            logger.info("Found group with the ID " + groupId);
            return ResponseEntity.status(HttpStatus.OK).body(events);
        } catch (IllegalArgumentException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("An error occured while getting the user's group.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to get the user's group.");
        }
    }

}
