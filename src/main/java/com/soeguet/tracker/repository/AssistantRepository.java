package com.soeguet.tracker.repository;

import com.soeguet.tracker.model.Assistant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssistantRepository extends JpaRepository<Assistant, UUID> {
  Optional<Assistant> findByNfcTagId(String uuid);
}
