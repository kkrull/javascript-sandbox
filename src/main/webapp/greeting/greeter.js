var Greeter = function() {
  if(!(this instanceof Greeter)) {
    throw 'Greeter constructor must be called with new';
  }
}

Greeter.prototype.sayHello = function(person) {
  var name = person && person.get('firstName');
  return 'Hello, ' + (name || 'world');
}