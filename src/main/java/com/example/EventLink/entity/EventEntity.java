package com.example.EventLink.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.math.BigDecimal;

@Entity
@Table(name = "events")
public class EventEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "external_event_id", nullable = false, unique = true)
    private String externalEventId;
    
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "venue_name", length = 255)
    private String venueName;
    
    @Column(name = "venue_city", length = 100)
    private String venueCity;
    
    @Column(name = "venue_state", length = 50)
    private String venueState;
    
    @Column(name = "venue_country", length = 50)
    private String venueCountry;
    
    @Column(name = "event_date")
    private LocalDate eventDate;
    
    @Column(name = "event_time")
    private LocalTime eventTime;
    
    @Column(name = "min_price", precision = 10, scale = 2)
    private BigDecimal minPrice;
    
    @Column(name = "max_price", precision = 10, scale = 2)
    private BigDecimal maxPrice;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Column(name = "category", length = 100)
    private String category;
    
    @Column(name = "genre", length = 100)
    private String genre;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(name = "event_url", length = 500)
    private String eventUrl;
    
    @Column(name = "external_source", nullable = false, length = 50)
    private String externalSource;
    
    // Default constructor
    public EventEntity() {}
    
    // Constructor for creating from external API data
    public EventEntity(String externalEventId, String title, String externalSource) {
        this.externalEventId = externalEventId;
        this.title = title;
        this.externalSource = externalSource;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getExternalEventId() {
        return externalEventId;
    }
    
    public void setExternalEventId(String externalEventId) {
        this.externalEventId = externalEventId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getVenueName() {
        return venueName;
    }
    
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
    
    public String getVenueCity() {
        return venueCity;
    }
    
    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }
    
    public String getVenueState() {
        return venueState;
    }
    
    public void setVenueState(String venueState) {
        this.venueState = venueState;
    }
    
    public String getVenueCountry() {
        return venueCountry;
    }
    
    public void setVenueCountry(String venueCountry) {
        this.venueCountry = venueCountry;
    }
    
    public LocalDate getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
    
    public LocalTime getEventTime() {
        return eventTime;
    }
    
    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }
    
    public BigDecimal getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
    
    public BigDecimal getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getEventUrl() {
        return eventUrl;
    }
    
    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }
    
    public String getExternalSource() {
        return externalSource;
    }
    
    public void setExternalSource(String externalSource) {
        this.externalSource = externalSource;
    }
    
    @Override
    public String toString() {
        return "EventEntity{" +
                "id=" + id +
                ", externalEventId='" + externalEventId + '\'' +
                ", title='" + title + '\'' +
                ", venueName='" + venueName + '\'' +
                ", eventDate=" + eventDate +
                ", eventTime=" + eventTime +
                ", category='" + category + '\'' +
                ", externalSource='" + externalSource + '\'' +
                '}';
    }
}