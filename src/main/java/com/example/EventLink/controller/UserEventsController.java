package com.example.EventLink.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.EventLink.entity.EventEntity;
import com.example.EventLink.entity.UserEntity;
import com.example.EventLink.entity.UserEventId;
import com.example.EventLink.entity.UserEventsEntity;
import com.example.EventLink.friendship.dto.UserEventRequest;
import com.example.EventLink.repository.EventRepository;
import com.example.EventLink.repository.UserEventsRepository;
import com.example.EventLink.repository.UserRepository;

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

    /**
     *  Return the full EventEntity list for a specific user.
     * This is what the "Friend's Events" screen will use.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventEntity>> getEventsForUser(@PathVariable Long userId) {
        List<UserEventsEntity> links = userEventsRepository.findByUserId(userId);

        if (links.isEmpty()) {
            // ok with empty list, so the frontend gets [] instead of 204
            return ResponseEntity.ok(List.of());
        }

        List<EventEntity> events = links.stream()
                .map(UserEventsEntity::getEvent) // uses the @ManyToOne event field
                .collect(Collectors.toList());

        return ResponseEntity.ok(events);
    }
}
