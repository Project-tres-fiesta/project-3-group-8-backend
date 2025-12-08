package com.example.EventLink.service;

import com.example.EventLink.dto.TicketmasterEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TicketmasterServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TicketmasterService ticketmasterService;

    private final String mockApiKey = "test-api-key";
    private final String mockBaseUrl = "https://app.ticketmaster.com/discovery/v2";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(ticketmasterService, "apiKey", mockApiKey);
        ReflectionTestUtils.setField(ticketmasterService, "baseUrl", mockBaseUrl);
        ReflectionTestUtils.setField(ticketmasterService, "restTemplate", restTemplate);
    }

    @Test
    void testSearchEvents_WithKeyword() {
        String mockResponse = """
            {
                "_embedded": {
                    "events": [
                        {
                            "id": "TM123",
                            "name": "Test Concert",
                            "url": "https://test.com",
                            "dates": {
                                "start": {
                                    "localDate": "2025-12-15",
                                    "localTime": "19:30:00"
                                }
                            },
                            "_embedded": {
                                "venues": [{
                                    "name": "Test Arena",
                                    "city": {"name": "Los Angeles"},
                                    "state": {"stateCode": "CA"},
                                    "country": {"countryCode": "US"}
                                }]
                            },
                            "classifications": [{
                                "segment": {"name": "Music"},
                                "genre": {"name": "Rock"}
                            }],
                            "images": [{"url": "https://image.com/test.jpg"}]
                        }
                    ]
                }
            }
            """;

        when(restTemplate.getForObject(any(URI.class), eq(String.class))).thenReturn(mockResponse);

        List<TicketmasterEvent> results = ticketmasterService.searchEvents("concert", null, null, null, null, 20);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("TM123", results.get(0).getId());
        assertEquals("Test Concert", results.get(0).getName());
        assertEquals("ticketmaster", results.get(0).getSource());
        verify(restTemplate, times(1)).getForObject(any(URI.class), eq(String.class));
    }

    @Test
    void testSearchEvents_EmptyResponse() {
        String mockResponse = "{\"_embedded\": {}}";
        when(restTemplate.getForObject(any(URI.class), eq(String.class))).thenReturn(mockResponse);

        List<TicketmasterEvent> results = ticketmasterService.searchEvents("nonexistent", null, null, null, null, 20);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testGetEventById() {
        String mockResponse = """
            {
                "id": "TM456",
                "name": "Single Event",
                "url": "https://test.com/event",
                "dates": {
                    "start": {
                        "localDate": "2025-12-20",
                        "localTime": "20:00:00"
                    }
                },
                "_embedded": {
                    "venues": [{
                        "name": "Stadium",
                        "city": {"name": "New York"},
                        "state": {"stateCode": "NY"},
                        "country": {"countryCode": "US"}
                    }]
                },
                "classifications": [{
                    "segment": {"name": "Sports"},
                    "genre": {"name": "Basketball"}
                }],
                "images": [{"url": "https://image.com/sports.jpg"}]
            }
            """;

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        TicketmasterEvent result = ticketmasterService.getEventById("TM456");

        assertNotNull(result);
        assertEquals("TM456", result.getId());
        assertEquals("Single Event", result.getName());
        assertEquals("Sports", result.getCategory());
    }

    @Test
    void testTestConnection_Success() {
        String mockResponse = "{\"_embedded\": {\"events\": []}}";
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockResponse);

        boolean result = ticketmasterService.testConnection();

        assertTrue(result);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }
}
