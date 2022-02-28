# `headlessui-reagent` in use

This directory contains an example of headless-reagent in use.

The code is in [src/main/all_examples.cljs](src/main/all_examples.cljs).

The components are all derived from [headlessui.dev](https://headlessui.dev),
the documentation site for Headless UI. On that site, each documentation page
includes a sample component at the top of the page. To the extent possible, our
components mirror those components. Deviations are documented in comments.

## Run

To view the live components, you'll need NPM version 7 or higher and
[babashka](https://github.com/babashka/babashka#installation). After these are
installed, run:

    $ bb live

When you see the output "Build completed", open
[http://localhost:8021](http://localhost:8021).

If you make changes to the ClojureScript code, they should appear in the browser
immediately.

### Development

You shouldn't need to restart the build very often, but if you do, you can speed
up the restarts by running:

    $ bb js-server
    
Wait for it to say "nREPL server started" before running `bb live` in a separate
terminal tab.
