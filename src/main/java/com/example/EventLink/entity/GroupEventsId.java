package com.example.EventLink.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GroupEventsId implements Serializable {

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "event_id")
    private Long eventId;

    public GroupEventsId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupEventsId that)) return false;
        return Objects.equals(groupId, that.groupId) && Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, eventId);
    }

    public GroupEventsId(Long groupId, Long eventId) {
        this.groupId = groupId;
        this.eventId = eventId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
