//Configures the Jasmine environment.
//Request path must match a context path defined in jasmine-maven-plugin::configuration/additionalContexts.
(function configureJasmineFixtures() {
  var path = 'webapp';
  console.debug('Configuring jasmine-jquery to request fixtures from /' + path);
  jasmine.getFixtures().fixturesPath = path;
})();