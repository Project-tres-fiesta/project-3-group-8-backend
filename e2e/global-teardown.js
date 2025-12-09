module.exports = async () => {
  console.log('🧹 Global E2E Teardown...');
  
  // Optional: Clean up any global resources
  // Note: We intentionally don't close browser sessions to preserve logins
  
  console.log('✅ Global E2E Teardown Complete (browser sessions preserved)');
};