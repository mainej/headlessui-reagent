# Contributing

TODO: establish contributing guidelines here.

## Deploy

### Prepare

1. Run `bin/preview-tag` to learn new version number.
2. Proactively update CHANGELOG.md for new version number.
3. Proactively update package.json for new version number.
4. Commit
5. Tag commit `bin/tag-release`

### Release

Deploy to Clojars -- needs `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` environment
variables:

    $ envdir ../../env/clojars bin/clojars-release

The library will be deployed to [clojars.org][clojars].

[clojars]: https://clojars.org/com.github.mainej/headlessui-reagent
