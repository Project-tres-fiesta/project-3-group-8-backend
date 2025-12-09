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
    // console.clear(); // Disabled for debugging visibility
    console.log("DEBUG: Script started...");
    console.log(`${colors.cyan}=====================================================${colors.reset}`);
    console.log(`${colors.bright}${colors.white}        JOSE ZAVALA - SECURITY & AUTHENTICATION      ${colors.reset}`);
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
    console.log(`   - Jose focused on Security Architecture and Authentication`);
    console.log(`   - Implemented JWT (JSON Web Token) generation and validation`);
    console.log(`   - Configured Spring Security filter chains and OAuth2 callbacks`);

    await waitForEnter("Show Jose's Commit History");
    const commitUrl = 'https://github.com/Project-tres-fiesta/project-3-group-8-backend/commits?author=JZavala210';
    if (githubDriver) await githubDriver.get(commitUrl);
    else exec(`start chrome "${commitUrl}"`);
    
    console.log(`\n${colors.magenta}TALKING POINTS (Commit History):${colors.reset}`);
    console.log(`   - Consistent contributions to security modules`);
    console.log(`   - Key commits: "Added JWT Filter", "Configured OAuth2"`);

    // 2. Code
    await waitForEnter("Open Code: JwtService.java (Token Logic)");
    exec('code "src/main/java/com/example/EventLink/service/JwtService.java"');
    
    console.log(`\n${colors.magenta}TALKING POINTS (JwtService.java):${colors.reset}`);
    console.log(`   - Core logic for signing and parsing tokens`);
    console.log(`   - Generates JWTs based on user details`);
    console.log(`   - Validates token signature and expiration`);
    
    await waitForEnter("Open Code: SecurityConfig.java (Filter Chain)");
    exec('code "src/main/java/com/example/EventLink/config/SecurityConfig.java"');
    
    console.log(`\n${colors.magenta}TALKING POINTS (SecurityConfig.java):${colors.reset}`);
    console.log(`   - Defines public vs protected routes`);
    console.log(`   - Disables CSRF for stateless API usage`);
    console.log(`   - Configures CORS for frontend integration`);
    
    await waitForEnter("Open Code: AuthController.java (Endpoints)");
    exec('code "src/main/java/com/example/EventLink/security/AuthController.java"');
    
    console.log(`\n${colors.magenta}TALKING POINTS (AuthController.java):${colors.reset}`);
    console.log(`   - Handles login and token distribution`);
    console.log(`   - Validates user credentials`);
    console.log(`   - Returns JWT to client upon success`);

    // 3. Live Test
    await waitForEnter("Run API Health Check & Security Test (Heroku)");
    
    console.log(`${colors.blue}   Testing Public Endpoint...${colors.reset}`);
    try {
        // Using /api/events/search since it is definitely public and exists
        const response = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/api/events/search?keyword=music');
        if (response.ok) {
             console.log(`${colors.green}   SUCCESS: Public endpoint accessible. Status: ${response.status}${colors.reset}`);
        } else {
             console.log(`${colors.red}   Public Test Error: Status ${response.status}${colors.reset}`);
        }
    } catch (error) {
        console.log(`${colors.red}   Public Test Error: ${error.message}${colors.reset}`);
    }

    console.log(`${colors.blue}   Testing Secured Endpoint (No Token)...${colors.reset}`);
    try {
        // /api/users/1 is public in SecurityConfig, so we use /api/auth/token which is secured
        const response = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/api/auth/token');
        if (response.status === 401 || response.status === 403) {
            console.log(`${colors.green}   SUCCESS: Access denied as expected (401/403)${colors.reset}`);
        } else {
            console.log(`${colors.yellow}   Unexpected status: ${response.status} (Should be 401/403)${colors.reset}`);
        }
    } catch (error) {
        console.log(`${colors.red}   Secured Test Error: ${error.message}${colors.reset}`);
    }
    
    console.log(`\n${colors.magenta}TALKING POINTS (Live API Test):${colors.reset}`);
    console.log(`   - Demonstrates that public routes are open`);
    console.log(`   - Proves that security filter chain blocks unauthorized access`);

    // 4. Frontend
    await waitForEnter("Launch Frontend Login Demo");
    
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
        
        // Navigate to home (Frontend 2.0)
        const frontendUrl = 'https://group8-frontend-7f72234233d0.herokuapp.com'; 
        await driver.get(frontendUrl + '/LoginPage');
        console.log(`${colors.green}   Frontend loaded (${frontendUrl})${colors.reset}`);
        
        console.log(`${colors.yellow}   BROWSER READY: Please demonstrate the feature manually.${colors.reset}`);
        
    } catch (error) {
        console.log(`${colors.red}   Browser Error: ${error.message}${colors.reset}`);
        if (driver) try { await driver.quit(); } catch (e) {}
    }
    
    console.log(`\n${colors.magenta}TALKING POINTS (Frontend):${colors.reset}`);
    console.log(`   - Login page connects to AuthController`);
    console.log(`   - Stores JWT in local storage/cookies`);
    console.log(`   - (You can now click 'Login' in the browser to show the error or success)`);

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
