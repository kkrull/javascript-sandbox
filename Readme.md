# JavaScript Sandbox

This project is a place for me to experiment with how to do JavaScript development in a Maven project.

Get started by running `mvn jasmine:bdd` and pointing your browser to `http://localhost:8234`.


## Testing

Jasmine tests are integrated with Maven via [jasmine-maven-plugin](http://searls.github.io/jasmine-maven-plugin/).  It
offers two modes of operation:

- Continuous operation for test-driving code via `mvn jasmine:bdd`: The tests run in a browser that you open and point
  to the address served by the local web server.
- As part of `mvn test`: The tests run headless (i.e. in an environment managed at the command-line, not in a browser).
  This is suitable for use in regular Maven workflows, including those on CI servers.

The default configuration for this plugin uses some HTTPUnit driver for Selenium, which I'm not so familiar with.
I've used PhantomJS before, and hence prefer to use that.  Normally you'd have to install Phantom on each environment
you plan to use, but [phantomjs-maven-plugin](https://github.com/klieber/phantomjs-maven-plugin) takes care of that
automatically when you run Maven.

Need more help?  Try `mvn jasmine:help -Ddetail=true` and `mvn jasmine:bdd -X`.  There are also lots of
[examples](https://github.com/searls/jasmine-maven-plugin/tree/master/src/test/resources/examples) of how to use
jasmine-maven-plugin.

### Configuration

I tried - and failed - to configure an additional `contextRoot` for `lib/main/javascript`, so that `lib/` can mirror
`src/`.  [Preloading resources](http://searls.github.io/jasmine-maven-plugin/test-mojo.html#preloadSources) does work,
although this requires files to be relative to `src/main/javascript`.  That's why third party libraries are - for now -
in `src/main/javascript/vendor` and `src/test/javascript/vendor`.

One such library is used only for testing: [jasmine-jquery](https://github.com/velesin/jasmine-jquery).  Note that the
version selected needs to be compatible with the version of Jasmine we're using (1.3).  There are some articles
explaining how to configure Jasmine for [loading templates](http://www.jayway.com/2012/04/17/configuring-jasmine-to-work-with-maven-and-jquery-fixtures/)
from external fixtures.

### IntelliJ live templates

```
afterEach(function() {
  $END$
});

beforeEach(function() {
  $END$
});

describe('$SUT$', function() {
  $END$
});

it('$BEHAVIOR$', function() {
  $END$
});
```


## Maven project structure

This project was generated using `jasmine-archetype`:

```
mvn archetype:generate -DarchetypeGroupId=com.github.searls -DarchetypeArtifactId=jasmine-archetype -DarchetypeVersion=1.3.1.5
```

This puts JavaScript files at `src/main/javascript` instead of the default `src/main/webapp` defined in the
[standard directory layout](http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).

For `mvn war:war` to include these sources in created WAR file, an entry for `src/main/javascript` has to be added to
`maven-war-plugin`'s configuration under `webResources/resource`.  These directories get copied straight into the WAR,
such that files related by feature get placed into the same subdirectory (i.e. `src/main/javascript/greeting/greeter.js`
and `src/main/css/greeting/greeting.css` both get copied to `greeting/` in the resulting WAR file).

As a result, it may be only be necessary to have `WEB-INF/web.xml` and index pages in `src/main/webapp` instead of
mixing up JavaScript, CSS, and JSP under a lower level of nesting.

### Third party libraries

The simplest approach with third party libraries is to check them in to version control along with source files and
reference these files like any other source file.  Third-party vendor libraries are in their respective language folder,
under a `vendor/` subdirectory and appear under `vendor/` in the packaged WAR.

[Webjars](http://www.webjars.org/documentation) can be configured for Maven projects (see "Servlet 3" section) in order
to manage client-side dependencies in a similar fashion to how server-side dependencies are managed.  There's also a
[sample project](https://github.com/webjars/sample-jetty_war) to help you get started.

This looks like a good idea for the long-term (and it's supported by the jasmine plugin), but it will add more
complexity than is initially warranted for small projects.

### IntelliJ integration

IntelliJ's features like auto-complete can use source for third party libraries:

- whose source is located in the project (just like any other file)
- made available through an explicitly-defined Maven webjar dependency.

`org.webjars.jasmine` is used by the Jasmine plugin, and hence is a plugin dependency (not a source code dependency).
It scope is listed as `provided` in the POM since the plugin may be using its own version.


## Modular source code (a.k.a. combining and minimizing)

Packages offer a variety of services, such as minifying and combining or are "script loaders".  They all allow you to
split up JavaScript and CSS source into multiple files, while providing some means to either make fewer HTTP requests
(by responding with concatenated source) or make those requests in parallel.  I've seen the former approach in use in a
Ruby on Rails environment with [Sprockets](https://github.com/sstephenson/sprockets#managing-and-bundling-dependencies),
which combines JavaScript files server-side, with a mechanism for declaring source file dependencies in JavaScript
comments.

These packages tend to emphasize the potential performance benefits on page loading, but the real goals here are to
allow source to be split up over multiple files and for those files to contain isolated, independently-maintainable
modules that separate public interface from implementation details.  The potential performance benefits just come along
for the ride.

One approach in Mavenland via [minify-maven-plugin](https://github.com/samaxes/minify-maven-plugin) uses the server-side
approach (similar to Sprocket) and appears to  rely upon the use of a global namespace.  It should be possible to use
Maven profiles to disable such minification during development, to make debugging easier.

Popular client-side solutions include:

- [RequireJS](https://github.com/jrburke/requirejs) implements the AMD spec, and is supported by the Jasmine plugin for
  Maven and by Backbone.  The authors claim that you can add this to legacy projects without modifying existing source.
- [HeadJS](http://headjs.com/)

For a small UI that's just starting out, it may be sufficient to explicitly load each source file with its own
`<script>` tag in the main page.  The Jasmine plugin for Maven loads all source and test files it finds in their
respective directories, so there's nothing preventing programmers from making multiple source files.

For further reading, see:

- [Single Page App Book](http://singlepageappbook.com/maintainability1.html)
- [Another blog post](http://www.gayadesign.com/front-end/better-javascript-dependency-management-with-browserify/)
  discusses the topic from a server-side perspective, such as would be seen in a node.js / CommonJS environment.


## Separation of concerns (Backbone and Handlebars)

[Backbone.js](http://backbonejs.org/) offers a means of separating client-side code into separate concerns of models,
collections, and views.  It can be used as a tool to provide these abstractions where it is useful, but it can also be
added one view / model / collection at a time to legacy projects without interfering with existing code.

Backbone Models represent the JSON structure exchanged with the server.  Some points of interest:

- Backbone handles the various kinds of RESTful HTTP requests necessary for CRUD operations on these entities when you
  configure a `url` on the model or collection.
- Models include default values, and can also perform client-side validation.
- Model developers are encouraged to keep the [Law of Demeter](http://en.wikipedia.org/wiki/Law_of_Demeter) in mind by
  adding model methods that decouple complex relationships from the particular JSON structure that happens to be used at
  the time.  In my experience, these JSON structures change frequently, so it's good to have methods to DRY up
  non-trivial traversals through the structure.

Backbone Views represent some component seen in the UI.  They are bound to an HTML element in the DOM and are backed by
a model or collection.  Backbone handles updating the DOM with the contents of the model when you implement a `render`
method.  The HTML itself is rendered client-side - from an HTML template and an instance of a model - using
[Handlebars.js](http://handlebarsjs.com/).

Views generally follow these patterns:

- Each view has an assigned HTML element, named `el` and wrapped/cached in jQuery via `$el`.
- An `initialize` method loads the HTML template with jQuery and compiles it with Handlebars.
- `render` uses this function returned from `Handlebars.compile` to convert a JavaScript object containing attributes of
  interest and returns an HTML structure to attach to the DOM, then it attaches it to the DOM with `this.$el.html`.
- Views can also attach event listeners to UI events.  If you do so, you have to be careful to preserve the meaning of
  `this` in the context in which these event listener functions are called (i.e. it should still refer to the instance
  of the view).  You do this by calling `_.bindAll` with the event listener method and an instance of the view in the
  `initialize` method.

For further reading, there's an [online book](http://addyosmani.github.io/backbone-fundamentals/) that describes
development and testing with Backbone.


## Future work

Production:

- An actual index page that renders the Backbone view.
- Use webjars to manage dependencies on JavaScript libraries without copying their source into source control.

Testing:

- JavaScript mocking libraries, such as SinonJS.
- Shared examples and shared context in Jasmine specs.