describe('sayHello', function() {
  describe('given no name', function() {
    it('does not freak out', function() {
      var greetNobody = function() { sayHello(undefined); };
      expect(greetNobody).not.toThrow();
    });
  });
  describe('given a name', function() {
    it('returns a greeting to the person with the specified name', function() {
      expect(sayHello('Frank')).toEqual('Hello, Frank');
    });
  });
});