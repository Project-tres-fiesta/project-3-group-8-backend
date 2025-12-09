package com.example.EventLink.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="group_members")
public class GroupMembersEntity {

    @EmbeddedId
    private GroupMembersId id;

    public GroupMembersEntity() {}

    public GroupMembersEntity(Long groupId, Long userId) {
        this.id = new GroupMembersId(groupId, userId);
    }


    public GroupMembersId getId() {
        return id;
    }

    public void setId(GroupMembersId id) {
        this.id = id;
    }
}
