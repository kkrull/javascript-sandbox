describe('GreeterView', function() {
  it('exists', function() {
    expect(GreeterView).toBeDefined();
  });
  xdescribe('.render', function() {
    var subject = new GreeterView();
    it('exists', function() {
      expect(subject.render()).not.toThrow();
    });
  });
});