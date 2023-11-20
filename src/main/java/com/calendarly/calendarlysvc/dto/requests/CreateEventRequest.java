package com.calendarly.calendarlysvc.dto.requests;

import com.calendarly.calendarlysvc.model.enums.Status;

import lombok.Getter;

@Getter
public class CreateEventRequest {
    private String title;
    private Integer creator;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
    private String category;
    private Status status;
    private Integer groupId;
}
