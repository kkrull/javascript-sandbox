//Configures the Jasmine environment.
//Request path must match a context path defined in jasmine-maven-plugin::configuration/additionalContexts.
(function configureJasmineFixtures() {
  //TODO KDK: Here - or in the test that needs it - load the template HTML and attach the HTML to the DOM.  Shouldn't need to worry about index.html's main-ish behavior.
  var path = 'src';
  console.debug('Configuring jasmine-jquery to request fixtures from /' + path);
  jasmine.getFixtures().fixturesPath = path;
})();