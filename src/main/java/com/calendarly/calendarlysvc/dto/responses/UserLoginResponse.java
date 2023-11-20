package com.calendarly.calendarlysvc.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponse {
    Integer userId;
    String responseMessage;
}
