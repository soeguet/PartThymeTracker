package com.soeguet.tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class WorkTime {
  @Id private final UUID id;
  private UUID assistantId;
  private boolean workingStatus;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String info;

  public WorkTime(
      UUID assistantId,
      LocalDateTime startTime,
      LocalDateTime endTime,
      boolean workingStatus,
      String info) {
    this.id = UUID.randomUUID();
    this.assistantId = assistantId;
    this.workingStatus = workingStatus;
    this.startTime = startTime;
    this.endTime = endTime;
    this.info = info;
  }

  public WorkTime() {
    this(null, null, null, false, null);
  }

  public UUID getId() {
    return id;
  }

  public boolean isWorkingStatus() {
    return endTime == null;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
    workingStatus = true;
    //        endTime = LocalDateTime.now();
  }

  public LocalDateTime getEndTime() {
    return Objects.requireNonNullElseGet(endTime, LocalDateTime::now);
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
    workingStatus = false;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public UUID getAssistantId() {
    return assistantId;
  }

  public void setAssistantId(UUID assistantId) {
    this.assistantId = assistantId;
  }

  public Duration getTotalTime() {
    return Duration.between(startTime, endTime == null ? LocalDateTime.now() : endTime);
  }

  @Override
  public String toString() {
    return "WorkTime{"
        + "id="
        + id
        + ", assistantId="
        + assistantId
        + ", workingStatus="
        + workingStatus
        + ", startTime="
        + startTime
        + ", endTime="
        + endTime
        + ", totalTime="
        + getTotalTime()
        + ", info='"
        + info
        + '\''
        + '}';
  }
}
