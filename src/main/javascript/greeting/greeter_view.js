var GreeterView = Backbone.View.extend({
  render: function() {
    var innerElement = $('<h2>');
    var greeting = 'Hello, ' + this.model.get('firstName');
    innerElement.text(greeting);
    this.$el.html(innerElement);
    return this;
  }
});