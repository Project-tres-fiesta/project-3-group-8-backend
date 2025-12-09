const { chromium } = require('playwright');
const path = require('path');

(async () => {
  const userDataDir = path.join(__dirname, 'playwright-user-data');
  const browser = await chromium.launchPersistentContext(userDataDir, {
    headless: false,
    slowMo: 300
  });
  const page = browser.pages()[0] || await browser.newPage();

  // Go to the login page
  await page.goto('https://group8-frontend-7f72234233d0.herokuapp.com');
  await page.waitForTimeout(2000);

  // Click the Google login button if present
  const loginBtn = await page.$('button:has-text("Google")');
  if (loginBtn) {
    console.log('Clicking Google login button...');
    await loginBtn.click();
    // Wait for user to complete login manually
    console.log('Please complete the Google login in the browser window.');
    await page.waitForTimeout(30000); // Wait 30 seconds for manual login
  } else {
    console.log('Already logged in or login button not found.');
  }

  // Do not close the browser so the user can keep it open
  console.log('Login session saved. You may now use the browser window.');
})();
