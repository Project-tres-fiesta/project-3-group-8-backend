package com.example.EventLink.repository;

import com.example.EventLink.entity.GroupMembersEntity;
import com.example.EventLink.entity.GroupMembersId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMembersRepository extends JpaRepository<GroupMembersEntity, GroupMembersId> {
    List<GroupMembersEntity> findByIdUserId(Long userId);
    List<GroupMembersEntity> findByIdGroupId(Long groupId);
}
