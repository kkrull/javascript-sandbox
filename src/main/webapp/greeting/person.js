var Person = Backbone.Model.extend({
  defaults: {
    firstName: ''
  },
  url: '/javascript-sandbox/person/name'
});