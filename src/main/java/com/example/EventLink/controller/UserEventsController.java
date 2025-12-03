package com.example.EventLink.controller;

import com.example.EventLink.entity.EventEntity;
import com.example.EventLink.entity.UserEntity;
import com.example.EventLink.entity.UserEventId;
import com.example.EventLink.entity.UserEventsEntity;
import com.example.EventLink.friendship.dto.UserEventRequest;
import com.example.EventLink.repository.EventRepository;
import com.example.EventLink.repository.UserEventsRepository;
import com.example.EventLink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-events")
public class UserEventsController {

    @Autowired
    private UserEventsRepository userEventsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostMapping
    public ResponseEntity<?> createUserEvent(@RequestBody UserEventRequest request) {

        // fetch user and event
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        EventEntity event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // create composite key
        UserEventId id = new UserEventId(request.getUserId(), request.getEventId());

        // create entity
        UserEventsEntity userEvent = new UserEventsEntity();
        userEvent.setId(id);
        userEvent.setUser(user);
        userEvent.setEvent(event);
        userEvent.setAttending(request.getAttending());
        userEvent.setWantsToGo(request.getWantsToGo());

        // save to DB
        userEventsRepository.save(userEvent);

        return ResponseEntity.ok(userEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<UserEventsEntity>> getUserEvents(@PathVariable Long id) {
        List<UserEventsEntity> events = userEventsRepository.findByUserId(id);

        if (events.isEmpty()) {
            return ResponseEntity.noContent().build(); // or ok with empty list
        }

        return ResponseEntity.ok(events);
    }
}
