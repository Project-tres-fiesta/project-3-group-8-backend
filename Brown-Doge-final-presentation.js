const { exec } = require('child_process');
const readline = require('readline');
const { Builder, By, until, Key } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');

// Silent check for chromedriver
try { require('chromedriver'); } catch (e) {}

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

const colors = {
    red: '\x1b[31m',
    green: '\x1b[32m',
    yellow: '\x1b[33m',
    blue: '\x1b[34m',
    magenta: '\x1b[35m',
    cyan: '\x1b[36m',
    white: '\x1b[37m',
    bright: '\x1b[1m',
    reset: '\x1b[0m'
};

async function waitForEnter(nextStep) {
    console.log(`\n\n${colors.yellow}-----------------------------------------------------${colors.reset}`);
    console.log(`${colors.cyan}NEXT ITEM: ${colors.bright}${colors.white}${nextStep}${colors.reset}`);
    console.log(`${colors.yellow}-----------------------------------------------------${colors.reset}`);
    return new Promise((resolve) => {
        rl.question(`${colors.green}Press ENTER to execute...${colors.reset}`, () => {
            resolve();
        });
    });
}

async function runDemo() {
    // console.clear();
    console.log("DEBUG: Script started...");
    console.log(`${colors.cyan}=====================================================${colors.reset}`);
    console.log(`${colors.bright}${colors.white}        ANDREW (BROWN-DOGE) - EVENTS & API          ${colors.reset}`);
    console.log(`${colors.cyan}=====================================================${colors.reset}`);
    
    // 1. GitHub
    await waitForEnter("Open GitHub Pulse (Browser)");
    
    let githubDriver;
    try {
        // Suppress ChromeDriver logs
        const service = new chrome.ServiceBuilder().setStdio('ignore');

        githubDriver = await new Builder()
            .forBrowser('chrome')
            .setChromeService(service)
            .setChromeOptions(new chrome.Options()
                .addArguments('--no-sandbox')
                .addArguments('--log-level=3')
                .excludeSwitches('enable-logging'))
            .build();
        await githubDriver.get('https://github.com/Project-tres-fiesta/project-3-group-8-backend/pulse');
    } catch (e) {
        exec(`start chrome "https://github.com/Project-tres-fiesta/project-3-group-8-backend/pulse"`);
    }
    
    console.log(`\n${colors.magenta}TALKING POINTS (GitHub Pulse):${colors.reset}`);
    console.log(`   - Andrew focused on External API Integration and Event Management`);
    console.log(`   - Implemented Ticketmaster API Service`);
    console.log(`   - Built the Event Controller for searching and filtering events`);

    await waitForEnter("Show Andrew's Commit History");
    const commitUrl = 'https://github.com/Project-tres-fiesta/project-3-group-8-backend/commits?author=Brown-Doge';
    if (githubDriver) await githubDriver.get(commitUrl);
    else exec(`start chrome "${commitUrl}"`);
    
    console.log(`\n${colors.magenta}TALKING POINTS (Commit History):${colors.reset}`);
    console.log(`   - Integrated Ticketmaster API`);
    console.log(`   - Created event search logic`);

    // 2. Code
    await waitForEnter("Open Code: TicketmasterService.java (API Logic)");
    exec('code "src/main/java/com/example/EventLink/service/TicketmasterService.java"');
    
    console.log(`\n${colors.magenta}TALKING POINTS (TicketmasterService.java):${colors.reset}`);
    console.log(`   - Handles communication with external Ticketmaster API`);
    console.log(`   - Parses JSON responses into internal Event objects`);
    
    await waitForEnter("Open Code: EventController.java (Endpoints)");
    exec('code "src/main/java/com/example/EventLink/controller/EventController.java"');
    
    console.log(`\n${colors.magenta}TALKING POINTS (EventController.java):${colors.reset}`);
    console.log(`   - Exposes events to the frontend`);
    console.log(`   - Supports search parameters (keywords, location)`);

    // 3. Live Test
    await waitForEnter("Run Event Search Test");
    
    console.log(`${colors.blue}   Searching for 'Concert' events...${colors.reset}`);
    try {
        const response = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/api/events/search?keyword=concert');
        if (response.ok) {
            const data = await response.json();
            console.log(`${colors.green}   SUCCESS: Found ${data.count || 'some'} events from Ticketmaster${colors.reset}`);
        } else {
            console.log(`${colors.red}   Event Search Error: Status ${response.status}${colors.reset}`);
        }
    } catch (error) {
        console.log(`${colors.red}   Event Search Error: ${error.message}${colors.reset}`);
    }
    
    console.log(`\n${colors.magenta}TALKING POINTS (Live API Test):${colors.reset}`);
    console.log(`   - Proves real-time data fetching from Ticketmaster`);
    console.log(`   - Shows backend proxying external API requests`);

    // 4. Frontend
    await waitForEnter("Launch Frontend Event Demo");
    
    if (githubDriver) {
        try { await githubDriver.quit(); } catch (e) {}
    }
    
    let driver;
    try {
        console.log(`${colors.cyan}   Launching Chrome...${colors.reset}`);
        const path = require('path');
        const userDataDir = path.resolve(__dirname, 'e2e/chrome-user-data');

        driver = await new Builder()
            .forBrowser('chrome')
            .setChromeOptions(new chrome.Options()
                .addArguments('--no-sandbox')
                .addArguments('--log-level=3')
                .addArguments(`--user-data-dir=${userDataDir}`))
            .build();
        
        const frontendUrl = 'https://group8-frontend-7f72234233d0.herokuapp.com';
        
        // --- STEP 1: EVENTS PAGE ---
        console.log(`${colors.cyan}   Navigating to Events Page...${colors.reset}`);
        await driver.get(frontendUrl + '/EventsPage');
        console.log(`${colors.green}   Frontend loaded (${frontendUrl})${colors.reset}`);
        
        console.log(`${colors.yellow}   BROWSER READY: Please demonstrate the feature manually.${colors.reset}`);
        
    } catch (error) {
        console.log(`${colors.red}   Browser Error: ${error.message}${colors.reset}`);
        if (driver) try { await driver.quit(); } catch (e) {}
    }
    
    console.log(`\n${colors.magenta}TALKING POINTS (Frontend):${colors.reset}`);
    console.log(`   - Browsed full application (Home -> Events -> Booked -> Profile)`);
    console.log(`   - Event discovery page uses the backend API`);
    console.log(`   - Displays event cards with images and details`);
    console.log(`   - (Browser is open for you to scroll and show results)`);

    await waitForEnter("Close Browser & Finish Demo");
    if (driver) try { await driver.quit(); } catch (e) {}

    console.log(`\n${colors.green}DEMO COMPLETE${colors.reset}`);
    rl.close();
    process.exit(0);
}

runDemo().catch(e => {
    console.error("Error:", e);
    process.exit(1);
});
