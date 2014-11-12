var Greeter = function() {
  if(!(this instanceof Greeter)) {
    throw 'Greeter constructor must be called with new';
  }
}

Greeter.prototype.sayHello = function(name) {
  return 'Hello, ' + (name || 'world');
}