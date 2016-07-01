package com.scuilion.confluence.macro.rest.dto;

import java.util.List;

public class StashProject {
    String key;
    int id;
    String name;
    List<StashRepo> repository;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<StashRepo> getRepository() {
        return repository;
    }

    public void setRepository(List<StashRepo> repository) {
        this.repository = repository;
    }
}
