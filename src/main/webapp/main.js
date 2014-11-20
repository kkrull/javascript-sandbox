//Script loading and intitialization for the production environment.  Loads with other scripts; runs on document ready.
(function main() {
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
    sandbox.environment.loadTemplate('greeting/templates.html', renderView);
  });
})();