package com.soeguet.tracker.model.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

public record WorkTimeAssistantDTO(UUID id, UUID assistantId, String assistantName, LocalDateTime startTime, LocalDateTime endTime, String workTime, String info) {

}
