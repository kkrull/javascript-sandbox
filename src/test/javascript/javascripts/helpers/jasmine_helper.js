(function() {
  var path = 'webapp';
  console.debug('Configuring jasmine fixtures to request from /' + path);
  jasmine.getFixtures().fixturesPath = path;
})();