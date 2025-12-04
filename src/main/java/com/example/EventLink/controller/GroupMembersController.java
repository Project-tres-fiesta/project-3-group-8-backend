package com.example.EventLink.controller;

import com.example.EventLink.entity.GroupMembersEntity;
import com.example.EventLink.repository.GroupMembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groupMembers")
public class GroupMembersController {
    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @PostMapping("/add")
    public ResponseEntity<GroupMembersEntity> addGroupMember(@RequestBody GroupMembersEntity groupMember) {
        // Save the membership
        GroupMembersEntity savedMembership = groupMembersRepository.save(groupMember);

        return ResponseEntity.ok(savedMembership);
    }


}
