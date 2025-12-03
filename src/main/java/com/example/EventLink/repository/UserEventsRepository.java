package com.example.EventLink.repository;

import com.example.EventLink.entity.UserEventsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserEventsRepository extends JpaRepository<UserEventsEntity, Long> {
    @Query("SELECT ue FROM UserEventsEntity ue WHERE ue.user.userId = :userId")
    List<UserEventsEntity> findByUserId(@Param("userId") Long userId);

}
