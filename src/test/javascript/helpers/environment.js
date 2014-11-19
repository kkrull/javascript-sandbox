//Emulate template loading done in main.js.  Loads with other scripts; runs on document ready.
$(function setupJasmine() {
  function configure() {
    var path = 'src';
    console.debug('Configuring jasmine-jquery to request fixtures from /' + path);
    jasmine.getFixtures().fixturesPath = path;
  }

  console.debug('Setting up test environment from environment.js');
  configure();
});