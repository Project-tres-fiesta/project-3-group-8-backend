const NodeEnvironment = require('jest-environment-node');

class DetoxEnvironment extends NodeEnvironment {
  constructor(config, context) {
    super(config, context);

    // Can be safely removed, if you are content with the default value (=300000ms)
    this.testTimeout = 120000;

    // This takes care of generating status logs on a per-spec basis. By default, jest only reports at file-level.
    // This is strictly optional.
    this.registerListeners = require('detox/runners/jest/listeners');
  }

  async setup() {
    await super.setup();
    await this.registerListeners(this.global);
  }

  async teardown() {
    await super.teardown();
  }
}

module.exports = DetoxEnvironment;