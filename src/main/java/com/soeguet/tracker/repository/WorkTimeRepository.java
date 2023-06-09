package com.soeguet.tracker.repository;

import com.soeguet.tracker.model.WorkTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkTimeRepository extends JpaRepository<WorkTime, UUID> {
  List<WorkTime> findAllByAssistantId(UUID assistantId);

  void deleteByAssistantId(UUID assistantId);
}
