package com.calendarly.calendarlysvc.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GroupCreatedResponse {
    Integer groupId;
    String groupCode;
    String responseMessage;
}
