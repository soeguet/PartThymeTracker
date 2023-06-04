package com.soeguet.tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Assistant {
    @Id
    private final UUID id;
    private String name;
    private String nfcTagId;

    public Assistant(String name, String nfcTagId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.nfcTagId = nfcTagId;
    }

    public Assistant(String name) {
        this(name, null);
    }

    public Assistant() {
        this(null, null);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNfcTagId() {
        return nfcTagId;
    }

    public void setNfcTagId(String nfcTagId) {
        this.nfcTagId = nfcTagId;
    }

    @Override
    public String toString() {
        return "Assistant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nfcTagId='" + nfcTagId + '\'' +
                '}';
    }
}
