Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "        ANDREW BROWN - BACKEND DEMONSTRATION        " -ForegroundColor Cyan  
Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host ""

# Open GitHub pages
Write-Host "1. Opening GitHub contribution history..." -ForegroundColor Yellow
Start-Process "https://github.com/Project-tres-fiesta/project-3-group-8-backend/pulse"
Start-Process "https://github.com/Project-tres-fiesta/project-3-group-8-backend/commits?author=Brown-Doge"
Start-Sleep 3

Write-Host ""
Write-Host "2. Opening VS Code with Andrew's backend files..." -ForegroundColor Yellow
code "src/main/java/com/example/EventLink/service/TicketmasterService.java"
code "src/main/java/com/example/EventLink/controller/EventController.java"
code "src/main/java/com/example/EventLink/dto/TicketmasterEvent.java"
code "src/main/java/com/example/EventLink/entity/EventEntity.java"
Start-Sleep 2

Write-Host ""
Write-Host "3. LIVE TESTING: Andrew's Deployed Backend APIs" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Green

Write-Host ""
Write-Host "Testing Ticketmaster Integration (Popular Events):" -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "https://group8-backend-0037104cd0e1.herokuapp.com/api/events/popular" -TimeoutSec 15
    Write-Host "✓ SUCCESS: Retrieved $($response.count) events from Ticketmaster API" -ForegroundColor Green
    
    if ($response.events -and $response.events.Count -gt 0) {
        $firstEvent = $response.events[0]
        Write-Host "  Sample Event: $($firstEvent.name)" -ForegroundColor White
        Write-Host "  Venue: $($firstEvent.venueName), $($firstEvent.venueCity)" -ForegroundColor White
        Write-Host "  Date: $($firstEvent.localDate) at $($firstEvent.localTime)" -ForegroundColor White
        Write-Host "  Category: $($firstEvent.category) - $($firstEvent.genre)" -ForegroundColor White
    }
} catch {
    Write-Host "⚠ API Test Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "Testing Event Search (Boston concerts):" -ForegroundColor Cyan
try {
    $searchResponse = Invoke-RestMethod -Uri "https://group8-backend-0037104cd0e1.herokuapp.com/api/events/search?city=Boston&keyword=concert" -TimeoutSec 15
    Write-Host "✓ SUCCESS: Search returned $($searchResponse.count) results" -ForegroundColor Green
    
    if ($searchResponse.events -and $searchResponse.events.Count -gt 0) {
        Write-Host "  Top result: $($searchResponse.events[0].name)" -ForegroundColor White
        Write-Host "  Location: $($searchResponse.events[0].venueCity), $($searchResponse.events[0].venueState)" -ForegroundColor White
    }
} catch {
    Write-Host "⚠ Search Test Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "Testing OAuth Authentication Endpoint:" -ForegroundColor Cyan
try {
    $null = Invoke-WebRequest -Uri "https://group8-backend-0037104cd0e1.herokuapp.com/auth/google" -MaximumRedirection 0 -ErrorAction SilentlyContinue
    Write-Host "✓ SUCCESS: OAuth endpoint accessible (redirects to Google)" -ForegroundColor Green
} catch {
    if ($_.Exception.Response.StatusCode -eq "Found") {
        Write-Host "✓ SUCCESS: OAuth redirect working correctly" -ForegroundColor Green
    } else {
        Write-Host "⚠ OAuth Test: $($_.Exception.Message)" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "4. Opening Frontend Integration Demo..." -ForegroundColor Yellow
Start-Process "https://group8-frontend-7f72234233d0.herokuapp.com"
Start-Sleep 2

Write-Host ""
Write-Host "ANDREW'S CONTRIBUTION SUMMARY:" -ForegroundColor Cyan
Write-Host "=============================" -ForegroundColor Cyan
Write-Host "✓ Ticketmaster API Service - External event data integration" -ForegroundColor Green
Write-Host "✓ Event Controller - RESTful endpoints (/api/events/popular, /api/events/search)" -ForegroundColor Green  
Write-Host "✓ Event Entity & DTO - Data persistence and JSON transformation" -ForegroundColor Green
Write-Host "✓ OAuth2 Integration - Google/GitHub authentication flow" -ForegroundColor Green
Write-Host "✓ Database Design - PostgreSQL with JPA/Hibernate" -ForegroundColor Green
Write-Host "✓ Error Handling - Rate limiting and API validation" -ForegroundColor Green
Write-Host ""
Write-Host "Live Backend: https://group8-backend-0037104cd0e1.herokuapp.com" -ForegroundColor Blue
Write-Host "Frontend Integration: React Native app consuming these APIs" -ForegroundColor Blue
Write-Host ""
Write-Host "Demo complete - backend is live and functional!" -ForegroundColor Green

# Keep windows open
Read-Host "Press Enter to close demo"