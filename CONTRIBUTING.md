# Contributing

TODO: establish contributing guidelines here.

## Upgrading @headlessui/react

1. Update package.json `"devDependencies"`
1. `npm install`
1. Update src/deps.cljs `:npm-deps`
1. Add new components to src/headlessui_reagent/core.clj
1. Update example/package.json `"dependencies"`
1. `cd example && npm install`
1. Test changes in example project
1. Update dev/build.clj `headlessui-react-version`

## Deploy

### Prepare

To prepare for a release, run `bin/check-release`. The script will report what
you can do to finish preparing, or if the release is ready, return an exit code
of 0.

It will check that you have done the following things:

1. Proactively updated CHANGELOG.md
2. Proactively updated package.json and ran `npm install`
3. Commited
4. Tagged commit


### Release

Deploy to Clojars -- needs `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` environment
variables:

    $ bin/clojars-release

The library will be deployed to [clojars.org][clojars].

[clojars]: https://clojars.org/com.github.mainej/headlessui-reagent
