package com.soeguet.tracker.model.DTOs;

import java.util.UUID;

public record AssistantOverviewDTO(UUID id, String name, String workTimeThisMonth, String workTimeLastMonth) {
}
