//Emulate template loading done in main.js.  Loads with other scripts; runs on document ready.
(function setupJasmine() {
  function configure() {
    var path = 'src';
    console.debug('Configuring jasmine-jquery to request fixtures from /' + path);
    jasmine.getFixtures().fixturesPath = path;
  }
  //
  //function loadTemplates() {
  //  loadFixtures('greeting/templates.html');
  //  logTemplates();
  //}
  //
  //function logTemplates() {
  //  var templates = $('#greeter_template');
  //  console.log('#greeter_template: ' + templates.length);
  //  _.map(templates, function(x) { console.log('- ' + x.id); });
  //}

  console.log('Setting up test environment from environment.js');
  configure();
  //$(function() { loadTemplates(); });
})();