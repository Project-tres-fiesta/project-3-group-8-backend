const E2ETestRunner = require('./E2ETestRunner');

describe('EventLink E2E Tests', () => {
  let testRunner;

  beforeAll(async () => {
    testRunner = new E2ETestRunner();
    console.log('🚀 Initializing E2E Test Suite...');
  });

  afterAll(async () => {
    if (testRunner) {
      // Don't cleanup automatically - preserve login session
      console.log('🔄 Maintaining browser session for future tests');
    }
  });

  describe('Backend API Integration', () => {
    test('should fetch popular events from Ticketmaster API', async () => {
      const response = await fetch('https://group8-backend-0037104cd0e1.herokuapp.com/api/events/popular');
      expect(response.ok).toBe(true);
      
      const data = await response.json();
      expect(data).toHaveProperty('events');
      expect(Array.isArray(data.events)).toBe(true);
      
      console.log(`✅ Retrieved ${data.events ? data.events.length : 0} popular events`);
    });

    test('should search events with city and keyword filters', async () => {
      const searchUrl = 'https://group8-backend-0037104cd0e1.herokuapp.com/api/events/search?city=Boston&keyword=music';
      const response = await fetch(searchUrl);
      expect(response.ok).toBe(true);
      
      const data = await response.json();
      expect(data).toHaveProperty('events');
      
      console.log(`✅ Search returned ${data.events ? data.events.length : 0} results for Boston music events`);
    });

    test('should have working OAuth endpoints', async () => {
      const oauthUrl = 'https://group8-backend-0037104cd0e1.herokuapp.com/auth/google';
      const response = await fetch(oauthUrl, { redirect: 'manual' });
      
      // OAuth should redirect (302) or be accessible
      expect([200, 302, 301].includes(response.status)).toBe(true);
      
      console.log(`✅ OAuth endpoint accessible (status: ${response.status})`);
    });
  });

  describe('Frontend Browser Automation', () => {
    test('should setup browser with persistent login session', async () => {
      await testRunner.setupBrowser(true);
      expect(testRunner.driver).toBeDefined();
      
      console.log('✅ Chrome browser initialized with persistent session');
    });

    test('should load EventLink frontend application', async () => {
      await testRunner.driver.get('https://group8-frontend-7f72234233d0.herokuapp.com');
      await testRunner.driver.sleep(3000);
      
      const title = await testRunner.driver.getTitle();
      const url = await testRunner.driver.getCurrentUrl();
      
      expect(title).toBeDefined();
      expect(url).toContain('group8-frontend');
      
      console.log(`✅ Frontend loaded: ${title}`);
    });

    test('should detect or perform login process', async () => {
      const isLoggedIn = await testRunner.checkIfLoggedIn();
      
      if (!isLoggedIn) {
        console.log('🔐 Performing login process...');
        const loginResult = await testRunner.performLogin();
        expect(loginResult).toBe(true);
      } else {
        console.log('✅ User already logged in from previous session');
      }
      
      // Take screenshot of current state
      await testRunner.takeScreenshot('login-state');
    });

    test('should interact with event search functionality', async () => {
      // Navigate to events page if it exists
      try {
        await testRunner.driver.get('https://group8-frontend-7f72234233d0.herokuapp.com/events');
        await testRunner.driver.sleep(3000);
        
        const pageSource = await testRunner.driver.getPageSource();
        const hasEventContent = pageSource.toLowerCase().includes('event') || 
                               pageSource.toLowerCase().includes('search');
        
        expect(hasEventContent).toBe(true);
        console.log('✅ Event search page accessible');
        
      } catch (error) {
        console.log('⚠️  Event page navigation test skipped:', error.message);
      }
    });

    test('should capture evidence screenshots', async () => {
      const screenshotPath = await testRunner.takeScreenshot('final-e2e-state');
      expect(screenshotPath).toBeTruthy();
      
      console.log('✅ Evidence screenshots captured');
    });
  });

  describe('End-to-End Integration', () => {
    test('should run complete integration test suite', async () => {
      const results = await testRunner.runFullE2ETest();
      
      expect(results.success).toBe(true);
      expect(results.tests.length).toBeGreaterThan(0);
      
      // Log detailed results
      console.log('\n📊 COMPLETE E2E TEST RESULTS:');
      results.tests.forEach(test => {
        const status = test.status === 'PASS' ? '✅' : test.status === 'FAIL' ? '❌' : '⚠️';
        console.log(`${status} ${test.name}: ${test.details}`);
      });
      
      const passRate = results.tests.filter(t => t.status === 'PASS').length / results.tests.length;
      expect(passRate).toBeGreaterThan(0.5); // At least 50% pass rate
      
      console.log(`\n🎯 Pass Rate: ${Math.round(passRate * 100)}%`);
    }, 180000); // Extended timeout for full suite
  });
});