package com.calendarly.calendarlysvc.model.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfo {
    private Integer userId;
    private String firstName;
    private String lastName; 
}
