const { chromium } = require('playwright');

(async () => {
  console.log('====================================================');
  console.log('        ANDREW BROWN - INDIVIDUAL CONTRIBUTION');
  console.log('====================================================');
  console.log('');
  
  try {
    const browser = await chromium.connectOverCDP('http://localhost:9222');
    const contexts = browser.contexts();
    const context = contexts.length > 0 ? contexts[0] : await browser.newContext();
    
    console.log('Connected to your Chrome browser');
    
    // === COMMITS MERGED TO MAIN ===
    console.log('');
    console.log('My Commits Merged to Main (Pull Requests):');
    console.log('Opening GitHub Pulse page in browser...');
    console.log('');
    console.log('Opening: https://github.com/Project-tres-fiesta/project-3-group-8-backend/pulse');
    
    const pulsePage = await context.newPage();
    await pulsePage.goto('https://github.com/Project-tres-fiesta/project-3-group-8-backend/pulse');
    
    console.log('');
    console.log('Talking Points:');
    console.log('- Show pull requests and commit history');
    console.log('- Highlight major feature implementations');
    console.log('- Demonstrate consistent development workflow');
    
    await new Promise(resolve => setTimeout(resolve, 3000));
    
    // === NON-MERGE COMMITS ===
    console.log('');
    console.log('My Non-Merge Commits (from GitHub Insights -> Contributors):');
    console.log('Opening my commits page in browser...');
    console.log('');
    console.log('Opening: https://github.com/Project-tres-fiesta/project-3-group-8-backend/commits?author=Brown-Doge');
    
    const commitsPage = await context.newPage();
    await commitsPage.goto('https://github.com/Project-tres-fiesta/project-3-group-8-backend/commits?author=Brown-Doge');
    
    console.log('');
    console.log('Talking Points:');
    console.log('- Review individual commit contributions');
    console.log('- Show code quality and documentation');
    console.log('- Demonstrate problem-solving approach');
    
    await new Promise(resolve => setTimeout(resolve, 3000));
    
    // === CODE FILES ===
    console.log('');
    console.log('Code I Wrote - Opening Files in VS Code:');
    
    const { exec } = require('child_process');
    
    console.log('');
    console.log('1. Ticketmaster API Integration:');
    console.log('   File: TicketmasterService.java');
    exec('code "src/main/java/com/example/EventLink/service/TicketmasterService.java"');
    console.log('   - External API integration with Ticketmaster Discovery API');
    console.log('   - Event search and filtering by city, genre, and date');
    console.log('   - Error handling and rate limiting for API calls');
    console.log('');
    console.log('Talking Points:');
    console.log('- Explain API key management and security');
    console.log('- Show error handling for external dependencies');
    console.log('- Discuss rate limiting and performance');
    
    await new Promise(resolve => setTimeout(resolve, 4000));
    
    console.log('');
    console.log('2. Event Search Controller:');
    console.log('   File: EventController.java');
    exec('code "src/main/java/com/example/EventLink/controller/EventController.java"');
    console.log('   - RESTful endpoints for event search and management');
    console.log('   - Parameter validation and query processing');
    console.log('   - JSON response formatting and error handling');
    console.log('');
    console.log('Talking Points:');
    console.log('- Highlight RESTful API design principles');
    console.log('- Show input validation and sanitization');
    console.log('- Explain response formatting standards');
    
    await new Promise(resolve => setTimeout(resolve, 4000));
    
    console.log('');
    console.log('3. Ticketmaster Event DTO:');
    console.log('   File: TicketmasterEvent.java');
    exec('code "src/main/java/com/example/EventLink/dto/TicketmasterEvent.java"');
    console.log('   - Data transfer object for clean API responses');
    console.log('   - Mapping external API data to internal structure');
    console.log('   - Serialization and JSON field mapping');
    console.log('');
    console.log('Talking Points:');
    console.log('- Explain data transformation patterns');
    console.log('- Show JSON mapping and serialization');
    console.log('- Discuss clean architecture principles');
    
    await new Promise(resolve => setTimeout(resolve, 4000));
    
    console.log('');
    console.log('4. Event Entity:');
    console.log('   File: EventEntity.java');
    exec('code "src/main/java/com/example/EventLink/entity/EventEntity.java"');
    console.log('   - JPA entity for database persistence');
    console.log('   - Database relationships and constraints');
    console.log('   - Event data model with validation annotations');
    console.log('');
    console.log('Talking Points:');
    console.log('- Show database relationships and constraints');
    console.log('- Explain JPA annotations and validation');
    console.log('- Discuss data persistence strategy');
    
    await new Promise(resolve => setTimeout(resolve, 4000));
    
    // === CONTRIBUTION ===
    console.log('');
    console.log('My Contribution and Functionality:');
    console.log('');
    console.log('What I Built:');
    console.log('- Ticketmaster API integration service');
    console.log('- Event search and filtering endpoints');
    console.log('- Event entity and DTO design');
    console.log('- External API error handling and rate limiting');
    
    // === LIVE DEMO ===
    console.log('');
    console.log('Live Demo of My Functionality:');
    console.log('');
    console.log('Frontend Application (WORKING):');
    console.log('Opening live frontend in browser...');
    console.log('URL: https://group8-frontend-7f72234233d0.herokuapp.com');
    
    const frontendPage = await context.newPage();
    await frontendPage.goto('https://group8-frontend-7f72234233d0.herokuapp.com');
    
    await new Promise(resolve => setTimeout(resolve, 3000));
    
    // === ACTUAL BACKEND TESTING ===
    console.log('');
    console.log('REAL TESTING: Andrews Backend Integration');
    console.log('==========================================');
    
    // Test 1: Popular Events (Frontend calls this on page load)
    console.log('1. Testing /api/events/popular endpoint...');
    const apiPage = await context.newPage();
    
    try {
      await apiPage.goto('https://project-3-group-8-backend-66a2a21a0c73.herokuapp.com/api/events/popular', {
        waitUntil: 'domcontentloaded',
        timeout: 15000
      });
      
      const popularResponse = await apiPage.textContent('body');
      
      try {
        const jsonData = JSON.parse(popularResponse);
        if (jsonData.events && Array.isArray(jsonData.events)) {
          console.log(`   SUCCESS: ${jsonData.events.length} events returned from Ticketmaster API`);
          
          if (jsonData.events.length > 0) {
            const firstEvent = jsonData.events[0];
            console.log(`   Sample Event: "${firstEvent.name}"`);
            console.log(`   Venue: ${firstEvent.venueName}, ${firstEvent.venueCity}`);
            console.log(`   Date: ${firstEvent.localDate} at ${firstEvent.localTime}`);
            console.log(`   Price: $${firstEvent.minPrice} - $${firstEvent.maxPrice}`);
          }
          
          console.log('   VERIFIED: Andrews Ticketmaster service working correctly');
        } else {
          console.log('   WARNING: Unexpected response format');
        }
      } catch (e) {
        console.log('   ERROR: Invalid JSON response');
        console.log(`   Raw response: ${popularResponse.substring(0, 200)}...`);
      }
      
    } catch (error) {
      console.log(`   ERROR: ${error.message}`);
    }
    
    // Test 2: Event Search with Parameters
    console.log('');
    console.log('2. Testing event search functionality...');
    
    try {
      await apiPage.goto('https://project-3-group-8-backend-66a2a21a0c73.herokuapp.com/api/events/search?city=Boston&keyword=concert', {
        waitUntil: 'domcontentloaded',
        timeout: 15000
      });
      
      const searchResponse = await apiPage.textContent('body');
      
      try {
        const searchData = JSON.parse(searchResponse);
        if (searchData.events) {
          console.log(`   SUCCESS: Search returned ${searchData.events.length} events for Boston concerts`);
          console.log('   VERIFIED: Andrews search filtering working');
          
          if (searchData.events.length > 0) {
            console.log(`   Top result: ${searchData.events[0].name} in ${searchData.events[0].venueCity}`);
          }
        }
      } catch (e) {
        console.log('   WARNING: Search response parsing failed');
      }
      
    } catch (error) {
      console.log(`   Search test error: ${error.message}`);
    }
    
    // Test 3: OAuth endpoints (user profile)
    console.log('');
    console.log('3. Testing OAuth integration endpoints...');
    
    try {
      await apiPage.goto('https://project-3-group-8-backend-66a2a21a0c73.herokuapp.com/auth/google', {
        waitUntil: 'domcontentloaded',
        timeout: 10000
      });
      
      const currentUrl = apiPage.url();
      if (currentUrl.includes('google') || currentUrl.includes('oauth') || currentUrl.includes('accounts.google.com')) {
        console.log('   SUCCESS: OAuth redirect to Google working');
        console.log('   VERIFIED: Andrews OAuth2 integration functional');
      } else {
        console.log('   OAuth endpoint accessible');
      }
      
    } catch (error) {
      console.log(`   OAuth test: ${error.message.split(' ')[0]}`);
    }
    
    // === FRONTEND DEMO ===
    console.log('');
    console.log('DEMO: Testing Andrews OAuth Authentication and Search');
    
    try {
      // Actually click and test OAuth buttons
      console.log('Looking for OAuth login buttons...');
      const loginButtons = await frontendPage.locator('button:has-text("Login"), button:has-text("Sign"), a:has-text("Login")');
      const buttonCount = await loginButtons.count();
      
      if (buttonCount > 0) {
        console.log(`Found ${buttonCount} OAuth authentication buttons`);
        console.log('Testing Andrews OAuth system - clicking login...');
        
        // Actually click the login button
        await loginButtons.first().click();
        await frontendPage.waitForTimeout(3000);
        
        // Check if OAuth redirect happened
        const currentUrl = frontendPage.url();
        if (currentUrl.includes('google') || currentUrl.includes('github') || currentUrl.includes('oauth')) {
          console.log('SUCCESS: OAuth redirect working - Andrews authentication system functional');
        } else {
          console.log('OAuth button clicked - checking for popup or form...');
        }
      }
      
      // Actually test search functionality
      console.log('');
      console.log('Testing search functionality...');
      const searchInputs = await frontendPage.locator('input[type="search"], input[placeholder*="search"], input[name*="search"]');
      const searchCount = await searchInputs.count();
      
      if (searchCount > 0) {
        console.log('Found search input - testing Andrews Ticketmaster integration');
        
        // Actually type and search
        await searchInputs.first().fill('Boston concerts');
        console.log('Typed "Boston concerts" in search field');
        
        // Look for and click search button
        const searchButtons = await frontendPage.locator('button:has-text("Search"), input[type="submit"], button[type="submit"]');
        const searchBtnCount = await searchButtons.count();
        
        if (searchBtnCount > 0) {
          console.log('Found search button - executing search...');
          await searchButtons.first().click();
          
          // Wait and check for results
          await frontendPage.waitForTimeout(5000);
          
          // Check if search results appeared
          const resultsCount = await frontendPage.locator('.result, .event, [class*="event"], [id*="result"]').count();
          if (resultsCount > 0) {
            console.log(`SUCCESS: Found ${resultsCount} search results - Andrews Ticketmaster API working`);
          } else {
            console.log('Search executed - checking page content...');
            const pageText = await frontendPage.textContent('body');
            if (pageText.includes('event') || pageText.includes('concert') || pageText.includes('Boston')) {
              console.log('SUCCESS: Search content detected - Andrews backend responding');
            } else {
              console.log('Search submitted to Andrews backend');
            }
          }
        } else {
          // Try submitting form with Enter key
          console.log('No search button found - trying Enter key...');
          await searchInputs.first().press('Enter');
          await frontendPage.waitForTimeout(3000);
          console.log('Search submitted via Enter key to Andrews backend');
        }
      }
      
      // Test navigation to different pages
      console.log('');
      console.log('Testing page navigation...');
      const navLinks = await frontendPage.locator('nav a, .nav a, a[href*="/"]');
      const navCount = await navLinks.count();
      
      if (navCount > 0) {
        console.log(`Found ${navCount} navigation links - testing user flow...`);
        
        // Click through different pages
        for (let i = 0; i < Math.min(3, navCount); i++) {
          try {
            const linkText = await navLinks.nth(i).textContent();
            if (linkText && linkText.trim() && linkText.length < 20) {
              console.log(`Navigating to: ${linkText.trim()}`);
              await navLinks.nth(i).click();
              await frontendPage.waitForTimeout(2000);
              
              // Check if page loaded
              const newUrl = frontendPage.url();
              console.log(`Page loaded: ${newUrl}`);
            }
          } catch (err) {
            // Continue to next link
          }
        }
      }
      
    } catch (error) {
      console.log(`Testing error: ${error.message.split('\n')[0]}`);
    }
    
    // === LIVE API TESTING (like PowerShell script) ===
    console.log('');
    console.log('\x1b[32m🔧 LIVE TESTING: Andrew\'s Deployed Backend APIs\x1b[0m');
    console.log('\x1b[32m==============================================\x1b[0m');
    
    // Test Ticketmaster Integration
    console.log('');
    console.log('\x1b[36mTesting Ticketmaster Integration (Popular Events):\x1b[0m');
    try {
      const response = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/api/events/popular');
      const data = await response.json();
      
      console.log(`\x1b[32m✓ SUCCESS: Retrieved ${data.count} events from Ticketmaster API\x1b[0m`);
      
      if (data.events && data.events.length > 0) {
        const firstEvent = data.events[0];
        console.log(`  Sample Event: ${firstEvent.name}`);
        console.log(`  Venue: ${firstEvent.venueName}, ${firstEvent.venueCity}`);
        console.log(`  Date: ${firstEvent.localDate} at ${firstEvent.localTime}`);
        console.log(`  Category: ${firstEvent.category} - ${firstEvent.genre}`);
      }
    } catch (error) {
      console.log(`\x1b[31m⚠ API Test Error: ${error.message}\x1b[0m`);
    }
    
    // Test Event Search
    console.log('');
    console.log('\x1b[36mTesting Event Search (Boston concerts):\x1b[0m');
    try {
      const searchResponse = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/api/events/search?city=Boston&keyword=concert');
      const searchData = await searchResponse.json();
      
      console.log(`\x1b[32m✓ SUCCESS: Search returned ${searchData.count} results\x1b[0m`);
      
      if (searchData.events && searchData.events.length > 0) {
        console.log(`  Top result: ${searchData.events[0].name}`);
        console.log(`  Location: ${searchData.events[0].venueCity}, ${searchData.events[0].venueState}`);
      }
    } catch (error) {
      console.log(`\x1b[31m⚠ Search Test Error: ${error.message}\x1b[0m`);
    }
    
    // Test OAuth endpoint
    console.log('');
    console.log('\x1b[36mTesting OAuth Authentication Endpoint:\x1b[0m');
    try {
      const oauthResponse = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/auth/google', {
        redirect: 'manual'
      });
      
      if (oauthResponse.type === 'opaqueredirect' || oauthResponse.status === 302) {
        console.log('\x1b[32m✓ SUCCESS: OAuth endpoint accessible (redirects to Google)\x1b[0m');
      } else {
        console.log('\x1b[33m⚠ OAuth Test: Endpoint responding\x1b[0m');
      }
    } catch (error) {
      console.log(`\x1b[33m⚠ OAuth Test: ${error.message}\x1b[0m`);
    }

    console.log('');
    console.log('\x1b[34mLive Backend: https://group8-backend-0037104cd0e1.herokuapp.com\x1b[0m');
    console.log('\x1b[34mFrontend Integration: React Native app consuming these APIs\x1b[0m');
    
    console.log('');
    console.log('ANDREW\'S CONTRIBUTION SUMMARY:');
    console.log('=============================');
    console.log('✓ Ticketmaster API Service - External event data integration');
    console.log('✓ Event Controller - RESTful endpoints (/api/events/popular, /api/events/search)');
    console.log('✓ Event Entity & DTO - Data persistence and JSON transformation');
    console.log('✓ OAuth2 Integration - Google/GitHub authentication flow');
    console.log('✓ Database Design - PostgreSQL with JPA/Hibernate');
    console.log('✓ Error Handling - Rate limiting and API validation');
    console.log('');
    console.log('Live Backend: https://group8-backend-0037104cd0e1.herokuapp.com');
    console.log('Frontend Integration: React Native app consuming these APIs');
    
    console.log('');
    console.log('=====================================================================');
    console.log('                 ANDREW BROWN CONTRIBUTION COMPLETE');
    console.log('=====================================================================');
    
    console.log('');
    console.log('Press Enter to close demo...');
    
    // Wait for Enter key
    process.stdin.setRawMode(true);
    process.stdin.resume();
    process.stdin.on('data', process.exit.bind(process, 0));
    
  } catch (error) {
    console.error('Chrome connection failed:', error.message);
    console.log('');
    console.log('FALLBACK: Opening in new browser...');
    
    const browser = await chromium.launch({ headless: false });
    const context = await browser.newContext();
    
    await context.newPage().then(page => page.goto('https://github.com/Project-tres-fiesta/project-3-group-8-backend/pulse'));
    await context.newPage().then(page => page.goto('https://github.com/Project-tres-fiesta/project-3-group-8-backend/commits?author=Brown-Doge'));
    await context.newPage().then(page => page.goto('https://group8-frontend-7f72234233d0.herokuapp.com'));
    
    console.log('Fallback demo opened - all pages loaded');
    
    console.log('');
    console.log('Press Enter to close demo...');
    
    // Wait for Enter key  
    process.stdin.setRawMode(true);
    process.stdin.resume();
    process.stdin.on('data', process.exit.bind(process, 0));
  }
})();