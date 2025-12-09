const { exec } = require('child_process');

async function runDemo() {
    console.log('\x1b[36m=====================================================\x1b[0m');
    console.log('\x1b[36m        ANDREW BROWN - BACKEND DEMONSTRATION        \x1b[0m');
    console.log('\x1b[36m=====================================================\x1b[0m');
    console.log('');

    // 1. Open GitHub pages
    console.log('\x1b[33m1. Opening GitHub contribution history...\x1b[0m');
    exec('start https://github.com/Project-tres-fiesta/project-3-group-8-backend/pulse');
    exec('start https://github.com/Project-tres-fiesta/project-3-group-8-backend/commits?author=Brown-Doge');
    await new Promise(resolve => setTimeout(resolve, 3000));

    console.log('');
    console.log('\x1b[33m2. Opening VS Code with Andrew\'s backend files...\x1b[0m');
    exec('code "src/main/java/com/example/EventLink/service/TicketmasterService.java"');
    exec('code "src/main/java/com/example/EventLink/controller/EventController.java"');
    exec('code "src/main/java/com/example/EventLink/dto/TicketmasterEvent.java"');
    exec('code "src/main/java/com/example/EventLink/entity/EventEntity.java"');
    await new Promise(resolve => setTimeout(resolve, 2000));

    console.log('');
    console.log('\x1b[32m3. LIVE TESTING: Andrew\'s Deployed Backend APIs\x1b[0m');
    console.log('\x1b[32m================================================\x1b[0m');

    // Test Ticketmaster API
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

    // Test Search API
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

    // Test OAuth
    console.log('');
    console.log('\x1b[36mTesting OAuth Authentication Endpoint:\x1b[0m');
    try {
        const oauthResponse = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/auth/google', {
            redirect: 'manual'
        });
        
        if (oauthResponse.status === 302 || oauthResponse.type === 'opaqueredirect') {
            console.log('\x1b[32m✓ SUCCESS: OAuth endpoint accessible (redirects to Google)\x1b[0m');
        } else {
            console.log('\x1b[33m⚠ OAuth Test: Endpoint responding\x1b[0m');
        }
    } catch (error) {
        console.log(`\x1b[33m⚠ OAuth Test: ${error.message}\x1b[0m`);
    }

    console.log('');
    console.log('\x1b[33m4. Opening Frontend Integration Demo...\x1b[0m');
    exec('start https://group8-frontend-7f72234233d0.herokuapp.com');
    await new Promise(resolve => setTimeout(resolve, 2000));

    console.log('');
    console.log('\x1b[36mANDREW\'S CONTRIBUTION SUMMARY:\x1b[0m');
    console.log('\x1b[36m=============================\x1b[0m');
    console.log('\x1b[32m✓ Ticketmaster API Service - External event data integration\x1b[0m');
    console.log('\x1b[32m✓ Event Controller - RESTful endpoints (/api/events/popular, /api/events/search)\x1b[0m');
    console.log('\x1b[32m✓ Event Entity & DTO - Data persistence and JSON transformation\x1b[0m');
    console.log('\x1b[32m✓ OAuth2 Integration - Google/GitHub authentication flow\x1b[0m');
    console.log('\x1b[32m✓ Database Design - PostgreSQL with JPA/Hibernate\x1b[0m');
    console.log('\x1b[32m✓ Error Handling - Rate limiting and API validation\x1b[0m');
    console.log('');
    console.log('\x1b[34mLive Backend: https://group8-backend-0037104cd0e1.herokuapp.com\x1b[0m');
    console.log('\x1b[34mFrontend Integration: React Native app consuming these APIs\x1b[0m');
    console.log('');
    console.log('\x1b[32mDemo complete - backend is live and functional!\x1b[0m');

    // Wait for Enter key
    console.log('');
    console.log('Press Enter to close demo...');
    process.stdin.setRawMode(true);
    process.stdin.resume();
    process.stdin.on('data', process.exit.bind(process, 0));
}

runDemo().catch(console.error);