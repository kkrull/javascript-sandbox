describe('GreeterView', function() {
  function domElement() { return $('#hook')[0];}

  beforeEach(function() {
    //TODO KDK: Find a way put the template somewhere more traditional, like src/main/webapp.  Then find a way to get jasmine-maven-plugin to serve up this additional context in a manner similar to and compatible with production.
    jasmine.getFixtures().fixturesPath = 'src';
    loadFixtures('templates.html');
    $('body').append($('<div id="hook">'));
  });
  afterEach(function() {
    $('#hook').remove();
  });

  describe('given a Person', function() {
    beforeEach(function() {
      this.subject = new GreeterView({
        el: domElement(),
        model: new Person({firstName: 'Frank'})
      });
    });

    describe('.remove', function() {
      describe('after rendering', function() {
        beforeEach(function() {
          this.subject.render();
          this.subject.remove();
        });
        it('removes itself from the given DOM container element', function() {
          expect($('#hook')).not.toBeInDOM();
        });
      });
    });

    describe('.render', function() {
      beforeEach(function() { this.returned = this.subject.render(); });
      it('renders the given model in the HTML template', function() {
        expect($('#hook')).toContainHtml('<h2>Hello, Frank</h2>');
      });
      it('returns itself', function() {
        expect(this.returned).toBe(this.subject);
      });
    });
  });
});