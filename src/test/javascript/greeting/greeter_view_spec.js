describe('GreeterView', function() {
  function domElement() { return $('#hook')[0];}

  beforeEach(function() {
    loadFixtures('greeting/templates.html');
    $('body').append($('<div id="hook">'));
    $('body').append($('<div id="main">'));
  });
  afterEach(function() {
    $('#hook').remove();
    $('#main').remove();
  });

  describe('.remove', function() {
    describe('after rendering', function() {
      beforeEach(function() {
        this.subject = new GreeterView({
          el: domElement(),
          model: new Person({firstName: 'Frank'})
        });
        this.subject.render();
        this.subject.remove();
      });
      it('removes itself from the given DOM container element', function() {
        expect($('#hook')).not.toBeInDOM();
      });
    });
  });

  describe('.render', function() {
    var subject;

    function doRender(targetSelector) {
      var viewConfig = targetSelector === undefined ? {} : {el: targetSelector};
      viewConfig.model = new Person({firstName: 'Frank'});
      subject = new GreeterView(viewConfig);
      return subject.render();
    }

    describe('always', function() {
      beforeEach(function() {
        this.returned = doRender(domElement());
      });
      it('renders the given model in the HTML template', function() {
        expect($('#hook')).toContainHtml('<h2>Hello, Frank.</h2>');
      });
      it('returns itself', function() {
        expect(this.returned).toBe(subject);
      });
    });

    describe('given an element', function() {
      it('renders the given model in the HTML template', function() {
        doRender(domElement());
        expect($('#hook')).not.toBeEmpty();
      });
    });

    describe('given no element', function() {
      it('renders the given model in #main', function() {
        doRender();
        expect($('#main')).not.toBeEmpty();
      });
    });
  });
});