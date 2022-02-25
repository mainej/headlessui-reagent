# `headlessui-reagent` in use

This directory contains an example of headless-reagent in use.

The code is in [src/main/all_examples.cljs](src/main/all_examples.cljs).

## Run

To view the live components, you'll need NPM version 7 or higher and
[babashka](https://github.com/babashka/babashka#installation). After these are
installed, run:

    $ bb live

After you see the output "Build completed", open
[http://localhost:8021](http://localhost:8021).

If you make changes to the ClojureScript code, they should appear in the browser
immediately.

### Development

You shouldn't need to restart the build very often, but if you do, you can speed
up the restarts by running:

    $ bb js-server
    
Wait for it to say "nREPL server started" before running `bb live` in a separate
terminal tab.
