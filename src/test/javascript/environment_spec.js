describe('Test environment', function() {
  describe('libraries', function() {
    it('includes Backbone', function() {
      expect(Backbone).toBeDefined();
    });
    it('includes jasmine-jquery', function() {
      expect($('body')).toBeInDOM();
    });
    it('includes jQuery', function() {
      expect(jQuery).toBeDefined();
      expect($).toBeDefined();
    });
    it('includes Underscore', function() {
      expect(_).toBeDefined();
    });
  });

  describe('jasmine test environment', function() {
    function addHook() { $('body').append($('<div id="hook">')); }

    function removeHook() { $('#hook').remove(); }

    afterEach(function() { removeHook(); });

    it('can add elements to the DOM and remove them again', function() {
      addHook();
      expect($('#hook')[0]).toBeDefined();
      removeHook();
      expect($('#hook')[0]).not.toBeDefined();
    });
    xit('can load HTML templates', function() {
      expect($('#greeter_template')[0]).toBeDefined();
    });
  });
});