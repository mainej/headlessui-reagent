# Contributing

TODO: establish contributing guidelines here.

## Upgrading @headlessui/react

1. Reference new version in README.md
1. Update package.json devDependencies
1. Update src/deps.cljs
1. Update dev/build.clj `build/format-version` to track upstream version.

## Deploy

### Prepare

To prepare for a release, run `bin/check-release`. The script will report what
you can do to finish preparing, or if the release is ready, return an exit code
of 0.

It will check that you have done the following things:

1. Proactively updated CHANGELOG.md
2. Proactively updated package.json
3. Commited
4. Tagged commit


### Release

Deploy to Clojars -- needs `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` environment
variables:

    $ envdir ../../env/clojars bin/clojars-release

The library will be deployed to [clojars.org][clojars].

[clojars]: https://clojars.org/com.github.mainej/headlessui-reagent
