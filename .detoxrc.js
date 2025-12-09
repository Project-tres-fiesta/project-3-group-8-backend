module.exports = {
  testRunner: "jest",
  runnerConfig: "e2e/config.json",
  apps: {
    eventlink: {
      type: "web.browser",
      url: "https://group8-frontend-7f72234233d0.herokuapp.com"
    }
  },
  devices: {
    chrome: {
      type: "web.chrome"
    }
  },
  configurations: {
    web: {
      device: "chrome",
      app: "eventlink"
    }
  }
};