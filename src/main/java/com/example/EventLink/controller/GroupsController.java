package com.example.EventLink.controller;

import com.example.EventLink.entity.GroupsEntity;
import com.example.EventLink.repository.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
public class GroupsController {

    @Autowired
    private GroupsRepository groupsRepository;

    @PostMapping
    public ResponseEntity<GroupsEntity> createGroup(@RequestBody GroupsEntity group){
        // Save group
        GroupsEntity savedGroup = groupsRepository.save(group);

        return ResponseEntity.ok(savedGroup);
    }
}
