package com.example.EventLink.repository;

import com.example.EventLink.entity.GroupEventsEntity;
import com.example.EventLink.entity.GroupMembersId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupEventsRepository extends JpaRepository<GroupEventsEntity, GroupMembersId> {
}
