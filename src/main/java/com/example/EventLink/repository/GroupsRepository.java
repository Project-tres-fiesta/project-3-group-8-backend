package com.example.EventLink.repository;

import com.example.EventLink.entity.GroupsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupsRepository extends JpaRepository<GroupsEntity, Long> {

    List<GroupsEntity> findByGroupName(String groupName);
}
