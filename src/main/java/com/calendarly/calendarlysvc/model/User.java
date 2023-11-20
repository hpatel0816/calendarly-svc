package com.calendarly.calendarlysvc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import com.calendarly.calendarlysvc.model.helpers.UserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Getter
@Setter
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(unique = true, length = 50)
    private String email;

    @Column(length = 100)
    private String password;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "user_group",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"),
        inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "groupId")
    )
    @JsonIgnoreProperties({"users", "events"})
    private Set<Group> groups = new HashSet<>();

    public User () {}

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public static UserInfo userInfo(User user) {
        return new UserInfo(user.getUserId(), user.getFirstName(), user.getLastName());
    }

}
