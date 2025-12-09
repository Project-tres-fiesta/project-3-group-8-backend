const { exec } = require('child_process');
const readline = require('readline');
const { Builder, By, until } = require('selenium-webdriver');
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
    console.log(`${colors.bright}${colors.white}        ANDRE GUTIERREZ - INFRASTRUCTURE & USERS     ${colors.reset}`);
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
    console.log(`   - Andre focused on Database Infrastructure and User Management`);
    console.log(`   - Implemented User Entities and Repositories`);
    console.log(`   - Created Database Test Controllers for health monitoring`);

    await waitForEnter("Show Andre's Commit History");
    const commitUrl = 'https://github.com/Project-tres-fiesta/project-3-group-8-backend/commits?author=andrewarriors739';
    if (githubDriver) await githubDriver.get(commitUrl);
    else exec(`start chrome "${commitUrl}"`);
    
    console.log(`\n${colors.magenta}TALKING POINTS (Commit History):${colors.reset}`);
    console.log(`   - Setup initial database schema`);
    console.log(`   - Maintained user management logic`);

    // 2. Code
    await waitForEnter("Open Code: UserEntity.java (Core Model)");
    exec('code "src/main/java/com/example/EventLink/entity/UserEntity.java"');
    
    console.log(`\n${colors.magenta}TALKING POINTS (UserEntity.java):${colors.reset}`);
    console.log(`   - Core data model for application users`);
    console.log(`   - Maps to the 'users' table in PostgreSQL`);
    
    await waitForEnter("Open Code: DbTestController.java (Health Check)");
    exec('code "src/main/java/com/example/EventLink/controller/DbTestController.java"');
    
    console.log(`\n${colors.magenta}TALKING POINTS (DbTestController.java):${colors.reset}`);
    console.log(`   - Critical infrastructure health check`);
    console.log(`   - Ensures database connectivity in production`);
    console.log(`   - Provides immediate feedback on system health`);

    // 3. Live Test
    await waitForEnter("Run Database Health Check");
    
    console.log(`${colors.blue}   Testing DB Connection...${colors.reset}`);
    try {
        const response = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/test-db');
        const text = await response.text();
        console.log(`${colors.green}   SUCCESS: ${text}${colors.reset}`);
    } catch (error) {
        console.log(`${colors.red}   DB Test Error: ${error.message}${colors.reset}`);
    }

    console.log(`${colors.blue}   Testing User API...${colors.reset}`);
    try {
        const response = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/api/users/1');
        console.log(`${colors.green}   SUCCESS: User API endpoint reachable (Status: ${response.status})${colors.reset}`);
    } catch (error) {
        console.log(`${colors.red}   User API Error: ${error.message}${colors.reset}`);
    }
    
    console.log(`\n${colors.magenta}TALKING POINTS (Live API Test):${colors.reset}`);
    console.log(`   - Confirms the database is online and reachable`);
    console.log(`   - Verifies user data retrieval`);

    // 4. Frontend
    await waitForEnter("Launch Frontend Profile Demo");
    
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
        await driver.get(frontendUrl + '/ProfilePage'); 
        console.log(`${colors.green}   Frontend loaded (${frontendUrl})${colors.reset}`);
        
        console.log(`${colors.yellow}   BROWSER READY: Please demonstrate the feature manually.${colors.reset}`);
        
    } catch (error) {
        console.log(`${colors.red}   Browser Error: ${error.message}${colors.reset}`);
        if (driver) try { await driver.quit(); } catch (e) {}
    }
    
    console.log(`\n${colors.magenta}TALKING POINTS (Frontend):${colors.reset}`);
    console.log(`   - Profile page displays data from UserEntity`);
    console.log(`   - Shows successful end-to-end data flow`);
    console.log(`   - (Browser is open for you to show the profile details)`);

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
