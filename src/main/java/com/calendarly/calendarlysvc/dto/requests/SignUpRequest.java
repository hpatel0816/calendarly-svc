package com.calendarly.calendarlysvc.dto.requests;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
