package com.example.EventLink.controller;

import com.example.EventLink.entity.GroupsEntity;
import com.example.EventLink.repository.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<GroupsEntity> getAllGroups() {
        return groupsRepository.findAll();
    }
}
