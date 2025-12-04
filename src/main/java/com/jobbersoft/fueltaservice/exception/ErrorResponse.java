package com.jobbersoft.fueltaservice.exception;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ErrorResponse {
    private String error;
    private OffsetDateTime timestamp;
}
