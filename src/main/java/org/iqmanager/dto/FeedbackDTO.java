package org.iqmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
public class FeedbackDTO {
    private long orderId;
    private String text;
    private byte stars;
}
