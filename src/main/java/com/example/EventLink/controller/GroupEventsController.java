package com.example.EventLink.controller;

import com.example.EventLink.entity.GroupEventsEntity;
import com.example.EventLink.repository.GroupEventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
