# `headlessui-reagent` in use

This directory contains an example of headless-reagent in use.

The code is in [src/main/all_examples.cljs](src/main/all_examples.cljs).

## Run once

To view the live components, execute

    $ bin/run

After you see the output "Build completed", open
[http://localhost:8021](http://localhost:8021).

## Refresh

If you plan to hack on the examples, you'll need two separate watches, one for
the CSS and one for the JS.

Run once:

    $ npm install
    $ bin/dev-html

Then, in one terminal:

    $ bin/dev-css --watch

And in another

    $ bin/dev-js

Again, wait for "Build completed", then open
[http://localhost:8021](http://localhost:8021).

