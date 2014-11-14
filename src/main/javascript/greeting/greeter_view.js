var GreeterView = Backbone.View.extend({
  initialize: function() {
    var source = '<h2>Hello, {{firstName}}</h2>';
    this.template = Handlebars.compile(source);
  },
  render: function() {
    this.$el.html(this.template(this.model.attributes));
    return this;
  }
});