$(function main() {
  var model = new Person({firstName: 'George'});
  var view = new GreeterView({model: model});
  view.render();
});