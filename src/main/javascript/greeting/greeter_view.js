var GreeterView = Backbone.View.extend({
  el: '#main',
  initialize: function() {
    var source = $('#greeter_template').text();
    this.template = Handlebars.compile(source);
  },
  render: function() {
    this.$el.html(this.template(this.model.attributes));
    return this;
  }
});