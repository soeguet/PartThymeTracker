package com.soeguet.tracker.repository;

import com.soeguet.tracker.model.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssistantRepository extends JpaRepository<Assistant, UUID> {

    Optional<Assistant> findByNfcTagId(String uuid);
}
