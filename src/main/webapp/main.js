$(function main() {
  function attachTemplateToDom(url) {
    $.get(url, function(content) {
      $('body').append(content);
    });
  }

  function renderView() {
    var model = new Person({firstName: 'George'});
    var view = new GreeterView({model: model});
    view.render();
  }

  console.log('Setting up production environment from main.js');
  attachTemplateToDom('greeting/templates.html');
  renderView();
});