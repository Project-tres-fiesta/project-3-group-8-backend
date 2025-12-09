/** @type {import('@jest/types').Config.InitialOptions} */
module.exports = {
  rootDir: '..',
  displayName: 'E2E Web Tests',
  testMatch: ['<rootDir>/e2e/**/*.e2e.test.js'],
  testTimeout: 120000,
  maxWorkers: 1,
  setupFilesAfterEnv: ['<rootDir>/e2e/setup.js'],
  verbose: true,
  collectCoverage: false,
  testEnvironment: 'node',
  
  // Global setup and teardown for browser management
  globalSetup: '<rootDir>/e2e/global-setup.js',
  globalTeardown: '<rootDir>/e2e/global-teardown.js',
  
  // Custom reporters for better output
  reporters: [
    'default',
    ['<rootDir>/e2e/custom-reporter.js', {}]
  ]
};