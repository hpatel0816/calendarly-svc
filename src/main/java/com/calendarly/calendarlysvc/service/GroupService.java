package com.calendarly.calendarlysvc.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.calendarly.calendarlysvc.model.Event;
import com.calendarly.calendarlysvc.model.Group;
import com.calendarly.calendarlysvc.model.User;
import com.calendarly.calendarlysvc.model.helpers.GroupInfo;
import com.calendarly.calendarlysvc.repository.GroupRepository;

@Service
public class GroupService {
    
    private final GroupRepository groupRepository;
    private final UserService userService;

    public GroupService(GroupRepository groupRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }


    public Group getGroupById(Integer groupId){
        return groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("The group doesn't exist."));
    }

    public Group getGroupByCode(String groupCode) {
        return groupRepository.findByGroupCode(UUID.fromString(groupCode))
        .orElseThrow(() -> new IllegalArgumentException("Invalid group code"));
    }

    public Group getGroup(String groupCode) {
        List<Group> groups = groupRepository.findAll();
        for (Group group: groups) {
            if (group.getGroupCode().toString().equals(groupCode)) {
                return group;
            }
        }
        return null;
    }

    public Set<GroupInfo> getAll(Integer userId) {
        Set<Group> groups = userService.getUserById(userId).getGroups();
        return groups.stream().map(Group::groupInfo).collect(Collectors.toSet());
    }

    public Set<Event> getGroupEvents(Integer groupId){
        return getGroupById(groupId).getEvents();
    }

    public void updateGroup(Group group) {
        groupRepository.save(group);
    }

    public Group createGroup(String groupName, Integer groupAdminId) {
        User groupAdmin = userService.getUserById(groupAdminId);
        Group newGroup = new Group(groupName, groupAdminId);
        newGroup.getUsers().add(groupAdmin);
        groupAdmin.getGroups().add(newGroup);
        return groupRepository.save(newGroup);
    }

    public void addGroupMember(String groupCode, Integer userId) {
        Group group = getGroupByCode(groupCode);
        User user = userService.getUserById(userId);
        group.getUsers().add(user);
        user.getGroups().add(group);
        groupRepository.save(group);
    }

}
