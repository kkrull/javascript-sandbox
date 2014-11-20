//Script loading and initialization for the production environment.  Loads with other scripts; runs on document ready.
(function main() {
  function renderView(updatedModel, _response, _options) {
    var view = new GreeterView({model: updatedModel});
    view.render();
  }

  $(function() {
    console.debug('Setting up production environment from main.js');
    sandbox.environment.loadTemplate('greeting/templates.html', function() {
      var model = new Person({id: 42});
      model.fetch({success: renderView});
    });
  });
})();