package com.example.EventLink.friendship.dto;

public class UserEventRequest {
    private Long userId;
    private Long eventId;
    private Boolean isAttending;
    private Boolean wantsToGo;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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
