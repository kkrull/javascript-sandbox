describe('Test environment', function() {
  describe('libraries', function() {
    it('includes Backbone', function() {
      expect(Backbone).toBeDefined();
    });
    it('includes jQuery', function() {
      expect(jQuery).toBeDefined();
    });
    it('includes Underscore', function() {
      expect(_).toBeDefined();
    });
  });

  describe('jasmine', function() {
    it('can add elements to the DOM', function() {
      $('body').append($('<div id="kdk">'));
      expect($('#kdk')).toBeDefined();

      $('#kdk').remove();
      expect($('#kdk')).not.toBeDefined();
    });
  });
});