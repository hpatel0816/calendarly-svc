package com.calendarly.calendarlysvc.dto.responses;

import com.calendarly.calendarlysvc.model.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EventCreatedResponse {
    Event event;
    String responseMessage;
}
