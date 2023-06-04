package com.soeguet.tracker.model.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkTimeDTO(UUID id,
                          UUID assistantId,
                          LocalDateTime startTime,
                          LocalDateTime endTime,
                          boolean workingStatus,
                          String info) {
}
