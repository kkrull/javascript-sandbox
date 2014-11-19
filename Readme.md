# JavaScript Sandbox

This project is a place for me to experiment with how to do JavaScript development in a Maven project.  You can do:

- Test-driven development in JavaScript: Run `mvn jasmine:bdd` and point your browser to `http://localhost:8234`.
- Regular tests and builds in Maven: `mvn test` includes Jasmine tests, and `mvn package` creates a WAR with all sources
  required to run the web application, just like a normal Maven workflow.

For a demo, run `mvn tomcat7:run` and go to `http://localhost:8080/javascript-sandbox`.


## Client-side script loading and context roots

In an ideal world, there would be absolutely no difference between production and test environments in:

- the URLs and context roots that are used to access resources
- the process and order of loading resources before starting to run

There are, however, some minor differences that can not be avoided at this time.  For loading sources, production uses
`<script>` tags in `index.html` to define the load order.  `jasmine-maven-plugin` is configured to load the same
dependencies in the same order, followed by any test-specific dependencies.

When it comes to loading Handlebars templates, `index.html` finishes by loading `main.js`, which fetches Handlebars
templates from `<feature>/templates.html` and attaches them to the DOM upon document ready.  `jasmine-maven-plugin` is
configured to *skip* loading `main.js`, due to the difference in context roots in production
(`<context-root>/<feature>`) and in Jasmine (`src/<feature>`).

**Each spec must load and attach its own templates, to maintain isolation**.  For example:

```
beforeEach(function() {
  loadFixtures('greeting/templates.html');
});
```


## Maven project structure

This project was generated using `jasmine-archetype`:

```
mvn archetype:generate -DarchetypeGroupId=com.github.searls -DarchetypeArtifactId=jasmine-archetype -DarchetypeVersion=1.3.1.5
```

This puts JavaScript files at `src/main/javascript` instead of the default `src/main/webapp` defined in the
[standard directory layout](http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).
Normally I'd prefer to separate by language then by feature, but this requires a lot of reconfiguration for other
plugins.

### Deployment via WAR

`maven-war-plugin` defines how to process and assemble source files into a WAR file.  You can then deploy this WAR to a
standalone instance of your favorite web server, or run an embedded web server via `mvn tomcat7:run-war` or
`mvn jetty:run-war`.  If you want to check the contents of the WAR, it's just a zip/jar file - try
`jar tf target/javascript-sandbox-<version>.war`.

Using the `run-war` goals mimics a production workflow, and gives you a chance to verify that the WAR gets assembled
correctly (i.e. everything still runs after it's been assembled into the WAR).  However, this isn't very handy if you're
in a development workflow and are changing source files rapidly.  For that you want to use `mvn jetty:run`, which loads
resources directly from `src/main/javascript` and `src/main/webapp` instead of copying them into a WAR file and running
the (old) copy.  `tomcat7-maven-plugin` does not appear to support multiple source paths for the same context, so it is
not compatible with a developer workflow on a project that has multiple source directories (it would work fine if
everything was munged together under `src/main/webapp`).

Finally, note that you can run both the Jasmine server and the Jetty/Tomcat server side by side.  They run on separate
ports, so there's no need to shut one down when starting the other.

### IntelliJ integration

IntelliJ's features like auto-complete can use source for third party libraries:

- whose source is located in the project (just like any other file).
- that are made available through an explicitly-defined Maven webjar dependency.

`org.webjars.jasmine` is used by the Jasmine plugin, and hence is a plugin dependency (not a source code dependency).
It scope is listed as `provided` in the POM since the plugin may be using its own version.

### Third party libraries

The simplest approach with third party libraries is to check them in to version control along with source files and
reference these files like any other source file.  Third-party JavaScript libraries therefore appear in
`src/main/webapp/vendor` and appear under `vendor/` in the packaged WAR.

[Webjars](http://www.webjars.org/documentation) can be configured for Maven projects (see "Servlet 3" section) in order
to manage client-side dependencies in a similar fashion to how server-side dependencies are managed.  There's also a
[sample project](https://github.com/webjars/sample-jetty_war) to help you get started.

This looks like a good idea for the long-term (and it's supported by the jasmine plugin), but it will add more
complexity than is initially warranted for small projects.


## Testing with Jasmine

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

### Attempted configuration: Separating source by language

An earlier experiment with separating by language was successful for `maven-war-plugin`.  For `mvn war:war` to include
these sources in created WAR file, an entry for `src/main/javascript` has to be added to `maven-war-plugin`'s
configuration under `webResources/resource`.  These directories get copied straight into the WAR, such that files
related by feature get placed into the same subdirectory (i.e. `src/main/javascript/greeting/greeter.js` and
`src/main/css/greeting/greeting.css` both get copied to `greeting/` in the resulting WAR file).  Resulting use of the
WAR in `mvn jetty:run-war` and `mvn tomcat7:run-war` was also successful, but it became difficult to configure the
bootstrapping process for Jasmine.

### Configuration: Vendor libraries

I tried - and failed - to configure an additional `contextRoot` for `lib/main/javascript`, so that `lib/` can mirror
`src/`.  [Preloading resources](http://searls.github.io/jasmine-maven-plugin/test-mojo.html#preloadSources) does work,
although this requires files to be relative to the JavaScript source directory (reconfigured to `src/main/javascript`).
That's why third party libraries are - for now - in `src/main/webapp/vendor` and `src/test/javascript/vendor`.

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

- Some real CRUD with a servlet, to show how Backbone handles that (and to get the java side working).
- Use webjars to manage dependencies on JavaScript libraries without copying their source into source control.

Testing:

- Run Jasmine tests directly in IntelliJ
- Cucumber for acceptance and/or integration tests?
- JavaScript mocking libraries, such as SinonJS.
- Shared examples and shared context in Jasmine specs.