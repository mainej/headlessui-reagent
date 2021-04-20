# Contributing

TODO: establish contributing guidelines here.

## Deploy

Build a deployable jar of this library:

    $ clojure -X:jar

This will update the generated `pom.xml` file to keep the dependencies synchronized with
your `deps.edn` file. You can update the version (and SCM tag) information in the `pom.xml` using the
`:version` argument:

    $ clojure -X:jar :version '"1.2.3"'

Install it locally (requires the `pom.xml` file):

    $ clojure -X:install

Deploy it to Clojars -- needs `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` environment
variables (requires the `pom.xml` file):

    $ clojure -X:deploy

Your library will be deployed to com.github.mainej/headlessui-reagent on clojars.org by default.

If you don't plan to install/deploy the library, you can remove the
`pom.xml` file but you will also need to remove `:sync-pom true` from the `deps.edn`
file (in the `:exec-args` for `depstar`).
