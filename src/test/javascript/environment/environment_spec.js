describe('sandbox.environment', function() {
  describe('.loadTemplate', function() {
    describe('given a url of a valid resource and a callback', function() {
      var callback = jasmine.createSpy('callback');
      beforeEach(function() {
        callback.reset();
        sandbox.environment.loadTemplate('spec/environment/template.html', callback);
        waitsFor(function() { return callback.callCount > 0; }, 'the callback to be invoked', 500);
      });
      afterEach(function() {
        $('#environment_spec_template').remove();
      });

      it('invokes the callback', function() {
        runs(function() { expect(callback).toHaveBeenCalled(); });
      });
      it('appends the loaded content to <head>', function() {
        runs(function() {
          expect($('head script#environment_spec_template')).toBeInDOM();
        });
      });
    });
  });
});