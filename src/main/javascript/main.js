$(function main() {
  //TODO KDK: This path exists as /webapp/greeting/templates.html under Jasmine
  $.get('greeting/templates.html', function(content) {
    console.log('Loading templates');

    $('body').append(content);
    var model = new Person({firstName: 'George'});
    var view = new GreeterView({model: model});
    view.render();
  });
});