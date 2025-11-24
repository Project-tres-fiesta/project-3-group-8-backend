package com.example.EventLink.repository;

import com.example.EventLink.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    
    // Find event by external API ID and source
    Optional<EventEntity> findByExternalEventIdAndExternalSource(String externalEventId, String externalSource);
    
    // Find events by external source (e.g., "ticketmaster")
    List<EventEntity> findByExternalSource(String externalSource);
    
    // Find events by category
    List<EventEntity> findByCategory(String category);
    
    // Find events by venue city
    List<EventEntity> findByVenueCity(String venueCity);
    
    // Find events by venue city and state
    List<EventEntity> findByVenueCityAndVenueState(String venueCity, String venueState);
    
    // Find events after a specific date
    List<EventEntity> findByEventDateAfter(LocalDate date);
    
    // Find events between date range
    List<EventEntity> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Search events by title containing keyword (case insensitive)
    @Query("SELECT e FROM EventEntity e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<EventEntity> findByTitleContainingIgnoreCase(@Param("keyword") String keyword);
    
    // Find events by category and city
    List<EventEntity> findByCategoryAndVenueCity(String category, String venueCity);
    
    // Check if event exists by external ID and source
    boolean existsByExternalEventIdAndExternalSource(String externalEventId, String externalSource);
}