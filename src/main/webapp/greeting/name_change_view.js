var NameChangeView = Backbone.View.extend({
  el: '#main',
  events: {
    'click button': 'changeName'
  },
  initialize: function() {
    var source = $('#name_change_template').text();
    this.template = Handlebars.compile(source);
    this.model = new Person({firstName: 'Adventurer'});
  },
  render: function() {
    this.$el.html(this.template(this.model.attributes));
    return this;
  },
  changeName: function(_event) {
    var firstName = $('#first_name').val();
    this.model.save({firstName: firstName});
  }
});