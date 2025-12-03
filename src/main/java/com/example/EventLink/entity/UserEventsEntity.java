package com.example.EventLink.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_events")
public class UserEventsEntity {
    @EmbeddedId
    private UserEventId id;

    // FOREIGN KEY (user_id) REFERENCES users(id)
    @MapsId("userId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // FOREIGN KEY (event_id) REFERENCES events(id)
    @MapsId("eventId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(name = "is_attending", nullable = false)
    private Boolean isAttending;

    @Column(name = "wants_to_go", nullable = false)
    private Boolean wantsToGo;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEventId getId() {
        return id;
    }

    public void setId(UserEventId id) {
        this.id = id;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public Boolean getAttending() {
        return isAttending;
    }

    public void setAttending(Boolean attending) {
        isAttending = attending;
    }

    public Boolean getWantsToGo() {
        return wantsToGo;
    }

    public void setWantsToGo(Boolean wantsToGo) {
        this.wantsToGo = wantsToGo;
    }
}
