(function main() {
  function attachTemplatesAndRenderView(url) {
    $.get(url, function(content) {
      $('head').append(content);
      renderView();
    });
  }

  function renderView() {
    var model = new Person({id: 42});
    model.fetch({
      success: function(updatedModel) {
        var view = new GreeterView({model: updatedModel});
        view.render();
      }
    });
  }

  $(function() {
    console.debug('Setting up production environment from main.js');
    attachTemplatesAndRenderView('greeting/templates.html');
  });
})();