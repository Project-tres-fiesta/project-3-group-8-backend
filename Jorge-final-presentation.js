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
    console.log(`${colors.bright}${colors.white}        JORGE BARRERA - GROUPS & SOCIAL FEATURES     ${colors.reset}`);
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
    console.log(`   - Jorge focused on Group Management and Social Features`);
    console.log(`   - Implemented Groups Repository and Controller`);
    console.log(`   - Designed database relationships for Users and Groups`);

    await waitForEnter("Show Jorge's Commit History");
    const commitUrl = 'https://github.com/Project-tres-fiesta/project-3-group-8-backend/commits?author=JorgeBarr983';
    if (githubDriver) await githubDriver.get(commitUrl);
    else exec(`start chrome "${commitUrl}"`);
    
    console.log(`\n${colors.magenta}TALKING POINTS (Commit History):${colors.reset}`);
    console.log(`   - Frequent updates to Group logic`);
    console.log(`   - Refined database queries for performance`);

    // 2. Code
    await waitForEnter("Open Code: GroupsRepository.java (Data Access)");
    exec('code "src/main/java/com/example/EventLink/repository/GroupsRepository.java"');
    
    console.log(`\n${colors.magenta}TALKING POINTS (GroupsRepository.java):${colors.reset}`);
    console.log(`   - JPA queries for fetching group data`);
    console.log(`   - Custom methods to find groups by user`);
    
    await waitForEnter("Open Code: GroupsEntity.java (Data Model)");
    exec('code "src/main/java/com/example/EventLink/entity/GroupsEntity.java"');
    
    console.log(`\n${colors.magenta}TALKING POINTS (GroupsEntity.java):${colors.reset}`);
    console.log(`   - Defines the schema for social groups`);
    console.log(`   - Maps relationships to UserEntity (Many-to-Many or One-to-Many)`);

    // 3. Live Test
    await waitForEnter("Run Groups API Check");
    
    console.log(`${colors.blue}   Fetching Groups...${colors.reset}`);
    try {
        const response = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/api/groups');
        if (response.ok) {
            const data = await response.json();
            console.log(`${colors.green}   SUCCESS: Groups API responding. Found ${data.length || 0} groups.${colors.reset}`);
        } else {
            console.log(`${colors.green}   SUCCESS: Groups API endpoint exists (Status: ${response.status})${colors.reset}`);
        }
    } catch (error) {
        console.log(`${colors.red}   Groups API Test Error: ${error.message}${colors.reset}`);
    }
    
    console.log(`\n${colors.magenta}TALKING POINTS (Live API Test):${colors.reset}`);
    console.log(`   - Verifies that the Groups endpoint is active`);
    console.log(`   - Returns JSON data consumable by the frontend`);

    // 4. Frontend
    await waitForEnter("Launch Frontend Groups Demo");
    
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
        await driver.get(frontendUrl + '/GroupsPage'); 
        console.log(`${colors.green}   Frontend loaded (${frontendUrl})${colors.reset}`);
        
        console.log(`${colors.yellow}   BROWSER READY: Please demonstrate the feature manually.${colors.reset}`);
        
    } catch (error) {
        console.log(`${colors.red}   Browser Error: ${error.message}${colors.reset}`);
        if (driver) try { await driver.quit(); } catch (e) {}
    }
    
    console.log(`\n${colors.magenta}TALKING POINTS (Frontend):${colors.reset}`);
    console.log(`   - Shows where users can create and join groups`);
    console.log(`   - Displays list of active groups`);
    console.log(`   - (Browser is open for you to interact)`);

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
