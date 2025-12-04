package com.example.EventLink.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="group_events")
public class GroupEventsEntity {
    @EmbeddedId
    private GroupEventsId id;

    public GroupEventsEntity() {}


    public GroupEventsEntity(GroupEventsId id) {
        this.id = id;
    }

    public GroupEventsId getId() {
        return id;
    }

    public void setId(GroupEventsId id) {
        this.id = id;
    }


}
