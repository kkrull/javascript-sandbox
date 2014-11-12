# JavaScript Sandbox

This project is a place for me to experiment with how to do JavaScript development in a Maven project.

## Maven structure

This project was generated using `jasmine-archetype`:

```
mvn archetype:generate -DarchetypeGroupId=com.github.searls -DarchetypeArtifactId=jasmine-archetype -DarchetypeVersion=1.3.1.5
```

## Testing

Jasmine tests are integrated with Maven via [jasmine-maven-plugin](http://searls.github.io/jasmine-maven-plugin/).  It
offers two modes of operation:

- Continuous operation for test-driving code via `mvn jasmine:bdd`: The tests run in a browser that you open and point
  to the address served by the local web server.
- As part of `mvn test`: The tests run headless (i.e. in an environment managed at the command-line, not in a browser).
  This is suitable for use in regular Maven workflows, including those on CI servers.

Need more help?  Try `mvn jasmine:help -Ddetail=true`.

The default configuration for this plugin uses some HTTPUnit driver for Selenium, which I'm not so familiar with.
I've used PhantomJS before, and hence prefer to use that.  Normally you'd have to install Phantom on each environment
you plan to use, but [phantomjs-maven-plugin](https://github.com/klieber/phantomjs-maven-plugin) takes care of that
automatically when you run Maven.

## IntelliJ integration

Some live templates are helpful:

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
which combines JavaScript files server-side, with a mechanism for declaring source file dependencies in JavaScript comments.

These packages tend to emphasize the potential performance benefits on page loading, but the real goals here are to
allow source to be split up over multiple files and for those files to contain isolated, independently-maintainable
modules that separate public interface from implementation details.  The potential performance benefits just come along
for the ride.

In the Ruby on Rails world, I've seen A similar approach in Mavenland may be
[minify-maven-plugin](https://github.com/samaxes/minify-maven-plugin), which appears to rely upon the use of a global
namespace.  It should be possible to use Maven profiles to disable such minification during development, to make
debugging easier.

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

## Items to explore

- Some framework to break the UI into distinct, composable views backed by data in model classes, ala Backbone.js
- Some basic library support, like underscore.js
- Some means of creating HTML from templates, like handlebars.js
- Combination and minification for production mode, to reduce requests down to 1 instead of 1 per .js file.