package com.example.EventLink.controller;

import com.example.EventLink.entity.GroupMembersEntity;
import com.example.EventLink.repository.GroupMembersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    // GET all user IDs for a given group
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Long>> getUsersByGroup(@PathVariable Long groupId) {
        List<Long> userIds = groupMembersRepository.findByIdGroupId(groupId)
                .stream()
                .map(member -> member.getId().getUserId())
                .collect(Collectors.toList());
        return ResponseEntity.ok(userIds);
    }
}
