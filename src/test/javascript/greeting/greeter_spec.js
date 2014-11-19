xdescribe('Greeter', function() {
  describe('constructor', function() {
    describe('when you forget to use new', function() {
        it('throws an error', function() {
          var callAsFunction = function() { Greeter(); }
          expect(callAsFunction).toThrow('Greeter constructor must be called with new');
      });
    });
  });

  describe('.sayHello', function() {
    var subject = new Greeter();

    describe('given undefined', function() {
      it('greets the world', function() {
        expect(subject.sayHello()).toEqual('Hello, world');
      });
    });
    describe('given a Person', function() {
      var person = new Person({firstName: 'Frank'});
      it('greets the Person by first name', function() {
        expect(subject.sayHello(person)).toEqual('Hello, Frank');
      });
    });
  });
});