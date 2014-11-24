//Script loading and initialization for the production environment.  Loads with other scripts; runs on document ready.
(function main() {
  $(function() {
    console.debug('Setting up production environment from main.js');
    sandbox.environment.loadTemplate('greeting/templates.html', function() {
      var view = new NameChangeView();
      view.render();
    });
  });
})();