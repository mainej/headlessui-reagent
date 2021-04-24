# Contributing

TODO: establish contributing guidelines here.

## Build

Build a deployable jar of this library:

    $ clojure -X:jar :version '"X.Y.Z"'

This will also update the generated `pom.xml` file with the version (and SCM
tag) and keep the dependencies synchronized with the `deps.edn` file.

(Optional) Install it locally (requires the `pom.xml` file):

    $ clojure -X:install
    
## Deploy

Confirm new version is present in pom.xml. (`project > version` and `project > scm > tag`)

Confirm CHANGELOG.md has been updated for new version.

Change version in package.json.

Confirm that everything is committed.

   $ git commit -am "vX.Y.Z"

Tag git head with version.

   $ git tag -a vX.Y.Z -m "vX.Y.Z"

Deploy to Clojars -- needs `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` environment
variables (requires the `pom.xml` file):

    $ clojure -X:deploy

The library will be deployed to [clojars.org][clojars].

Push to github, with tags:

    $ git push --tags

TODO: combine build and deploy into scripts, which include automated checks that
version is present everywhere it needs to be.

[clojars]: https://clojars.org/com.github.mainej/headlessui-reagent
