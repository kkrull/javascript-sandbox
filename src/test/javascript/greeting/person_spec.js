describe('Person', function() {
  describe('default attributes', function() {
    var subject = new Person();
    it('.firstName is ""', function() {
      expect(subject.get('firstName')).toEqual('');
    });
  });
});