// Jest setup for E2E web tests
const { execSync } = require('child_process');

// Global test configuration
global.console = {
  ...console,
  log: jest.fn((message) => {
    // Always show test output in real time
    process.stdout.write(message + '\n');
  })
};

// Ensure Chrome driver is available
beforeAll(() => {
  try {
    execSync('chromedriver --version', { stdio: 'ignore' });
  } catch (error) {
    console.log('⚠️  ChromeDriver not found in PATH. Make sure chromedriver is installed.');
    console.log('   You can install it with: npm install -g chromedriver');
  }
});

// Global error handling
process.on('unhandledRejection', (reason, promise) => {
  console.log('❌ Unhandled Rejection at:', promise, 'reason:', reason);
});

process.on('uncaughtException', (error) => {
  console.log('❌ Uncaught Exception:', error);
});

// Add custom matchers if needed
expect.extend({
  toBeValidUrl(received) {
    try {
      new URL(received);
      return {
        message: () => `expected ${received} not to be a valid URL`,
        pass: true,
      };
    } catch (error) {
      return {
        message: () => `expected ${received} to be a valid URL`,
        pass: false,
      };
    }
  },
});