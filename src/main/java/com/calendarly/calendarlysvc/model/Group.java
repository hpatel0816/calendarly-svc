package com.calendarly.calendarlysvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.calendarly.calendarlysvc.model.helpers.GroupInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Getter
@Setter
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer groupId;

    @Column(length = 20, unique = true)
    private String groupName;

    private UUID groupCode;

    private Integer groupAdmin;

    @ManyToMany(mappedBy = "groups", cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties("groups")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties("group")
    private Set<Event> events = new HashSet<>();

    public Group() {}

    public Group(String groupName, Integer groupAdmin) {
        this.groupName = groupName;
        this.groupAdmin = groupAdmin;
        this.groupCode = UUID.randomUUID();
    }

    public static GroupInfo groupInfo(Group group) {
        return new GroupInfo(group.getGroupId(), group.getGroupName());
    }
    
}
