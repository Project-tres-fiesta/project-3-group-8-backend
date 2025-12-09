class CustomReporter {
  constructor(globalConfig, options) {
    this._globalConfig = globalConfig;
    this._options = options;
  }

  onRunStart(results, options) {
    console.log('\n🎯 EVENTLINK E2E TEST SUITE');
    console.log('===========================');
    console.log(`Tests: ${results.numTotalTestSuites} suites`);
    console.log(`Time: ${new Date().toLocaleTimeString()}`);
  }

  onTestResult(test, testResult, aggregatedResult) {
    const { testFilePath } = test;
    const filename = testFilePath.split(/[\\\/]/).pop();
    
    if (testResult.numFailingTests > 0) {
      console.log(`❌ ${filename}: ${testResult.numFailingTests} failed`);
    } else {
      console.log(`✅ ${filename}: ${testResult.numPassingTests} passed`);
    }
  }

  onRunComplete(contexts, results) {
    const { numTotalTests, numPassedTests, numFailedTests, startTime } = results;
    const duration = Date.now() - startTime;
    
    console.log('\n📊 E2E TEST SUMMARY');
    console.log('==================');
    console.log(`✅ Passed: ${numPassedTests}`);
    console.log(`❌ Failed: ${numFailedTests}`);
    console.log(`⏱️  Duration: ${duration}ms`);
    console.log(`🎯 Success Rate: ${Math.round((numPassedTests / numTotalTests) * 100)}%`);
    
    if (numFailedTests === 0) {
      console.log('\n🎉 ALL E2E TESTS PASSED! 🎉');
    } else {
      console.log('\n⚠️  Some tests failed. Check output above for details.');
    }
  }
}

module.exports = CustomReporter;