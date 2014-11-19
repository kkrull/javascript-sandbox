(function main() {
  function attachTemplatesAndRenderView(url) {
    $.get(url, function(content) {
      $('body').append(content);
      renderView();
    });
  }

  function renderView() {
    var model = new Person({firstName: 'George'});
    var view = new GreeterView({model: model});
    view.render();
  }

  $(function() {
    console.debug('Setting up production environment from main.js');
    attachTemplatesAndRenderView('greeting/templates.html');
  });
})();