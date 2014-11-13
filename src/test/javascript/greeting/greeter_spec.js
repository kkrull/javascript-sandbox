describe('Greeter', function() {
  describe('constructor', function() {
    describe('when you forget to use new', function() {
        it('throws an error', function() {
          var invokeDirectly = function() { Greeter(); }
          expect(invokeDirectly).toThrow('Greeter constructor must be called with new');
      });
    });
  });

  describe('sayHello', function() {
    var subject = new Greeter();

    describe('given no name', function() {
      it('greets the world', function() {
        expect(subject.sayHello()).toEqual('Hello, world');
      });
    });
    describe('given a name', function() {
      it('greets the person with the specified name', function() {
        expect(subject.sayHello('Frank')).toEqual('Hello, Frank');
      });
    });
  });
});