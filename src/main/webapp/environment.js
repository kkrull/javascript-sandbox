var sandbox = sandbox || {};
$.extend(true, sandbox, {
  environment: {
    loadTemplate: function(url, onSuccess) {
      $.get(url, function(template) {
        $('head').append(template);
        onSuccess();
      });
    }
  }
});