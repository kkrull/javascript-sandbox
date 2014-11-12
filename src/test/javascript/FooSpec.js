describe('Foo', function() {
  it('exists', function() {
    expect(Foo).toBeDefined();
  });
  it('can use external libraries', function() {
    expect(_).toBeDefined(); //TODO KDK: Work here to include webjars source
  });
});