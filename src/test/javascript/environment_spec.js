describe('Test environment', function() {
  describe('libraries', function() {
    it('includes Backbone', function() {
      expect(Backbone).toBeDefined();
    });
    it('includes jQuery', function() {
      expect(jQuery).toBeDefined();
      expect($).toBeDefined();
    });
    it('includes Underscore', function() {
      expect(_).toBeDefined();
    });
  });

  describe('jasmine', function() {
    afterEach(function() {
      $('#hook').remove();
    });
    it('can add elements to the DOM', function() {
      $('body').append($('<div id="hook">'));
      expect($('#hook')[0]).toBeDefined();
    });
    it('can remove elements from the DOM', function() {
      $('#hook').remove();
      expect($('#hook')[0]).not.toBeDefined();
    });
  });
});