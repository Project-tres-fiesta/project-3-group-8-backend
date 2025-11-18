package com.example.EventLink.friendship;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.EventLink.friendship.dto.FriendRequestCreate;
import com.example.EventLink.friendship.dto.FriendRequestDecision;
import com.example.EventLink.friendship.dto.FriendshipDto;
import com.example.EventLink.security.CurrentUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/friendships")
@Validated
public class FriendshipController {

    private final FriendshipService service;
    private final CurrentUserService currentUser;

    public FriendshipController(FriendshipService service, CurrentUserService currentUser) {
        this.service = service;
        this.currentUser = currentUser;
    }

    /** ✅ Send friend request as the authenticated user */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FriendshipDto send(Authentication auth, @Valid @RequestBody FriendRequestCreate body) {
        Integer requesterId = currentUser.userIdFromAuth(auth);
        return service.sendRequest(requesterId, body);
    }

    /** ✅ Accept or reject a friend request */
    @PutMapping("/{id}")
    public FriendshipDto decide(Authentication auth,
                                @PathVariable("id") Integer friendshipId,
                                @Valid @RequestBody FriendRequestDecision body) {
        Integer actingUserId = currentUser.userIdFromAuth(auth);
        return service.decide(actingUserId, friendshipId, body);
    }

    /** ✅ List all friendships for the current user */
    @GetMapping
    public List<FriendshipDto> list(Authentication auth) {
        Integer userId = currentUser.userIdFromAuth(auth);
        return service.listForUser(userId);
    }

    /** ✅ List pending friendship requests for the current user */
    @GetMapping("/pending")
    public List<FriendshipDto> pending(Authentication auth) {
        Integer userId = currentUser.userIdFromAuth(auth);
        return service.pendingForUser(userId);
    }

    /** ✅ Delete friendship */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer friendshipId, Authentication auth) {
        Integer userId = currentUser.userIdFromAuth(auth);
        service.delete(friendshipId, userId);
    }
}


