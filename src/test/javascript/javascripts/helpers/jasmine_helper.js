//TODO KDK: Find a way put the template somewhere more traditional, like src/main/webapp.  Then find a way to get jasmine-maven-plugin to serve up this additional context in a manner similar to and compatible with production.
console.log('Configuring jasmine');
jasmine.getFixtures().fixturesPath = 'src';