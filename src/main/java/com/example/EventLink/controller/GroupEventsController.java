package com.example.EventLink.controller;

import com.example.EventLink.entity.GroupEventsEntity;
import com.example.EventLink.repository.GroupEventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groupEvents")
public class GroupEventsController {

    @Autowired
    private GroupEventsRepository groupEventsRepository;

    @PostMapping("/add")
    public ResponseEntity<GroupEventsEntity> addGroupEvent(@RequestBody GroupEventsEntity groupEvent) {
        // Save the membership
        GroupEventsEntity savedEvent = groupEventsRepository.save(groupEvent);

        return ResponseEntity.ok(savedEvent);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<List<Long>> getEventIdsByGroup(@PathVariable Long groupId) {
        List<Long> eventIds = groupEventsRepository.findByIdGroupId(groupId)
                .stream()
                .map(entity -> entity.getId().getEventId())
                .collect(Collectors.toList());
        return new ResponseEntity<>(eventIds, HttpStatus.OK);
    }

}
