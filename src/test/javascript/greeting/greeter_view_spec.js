describe('GreeterView', function() {
  function domElement() {
    return $('#hook')[0];
  }

  beforeEach(function() {
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
      beforeEach(function() {
        this.subject.remove();
      });
      it('removes the element from the DOM', function() {
        expect(domElement()).not.toBeDefined();
      });
    });

    describe('.render', function() {
      beforeEach(function() {
        this.returned = this.subject.render();
      });
      it('renders the template', function() {
        expect($('div h2').text()).toEqual('Hello, Frank');
      });
      it('returns itself', function() {
        expect(this.returned).toBe(this.subject);
      });
    });
  });
});