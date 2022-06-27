# Change Log
All notable changes to this project will be documented in this file. This
change log follows the conventions of
[keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]

## [1.6.5.70]
- Upgraded to @headlessui/react 1.6.5

## [1.6.4.68]
- Upgraded to @headlessui/react 1.6.4

## [1.6.3.67]
- Upgraded to @headlessui/react 1.6.3

## [1.6.2.64]
- Upgraded to @headlessui/react 1.6.2

## [1.6.1.63]
- Upgraded to @headlessui/react 1.6.1

## [1.6.0.62]
### Changed
- Upgraded to @headlessui/react 1.6.0
- Upgraded to reagent 1.1.1
### Added
- Reagent interop for Dialog.Panel and Dialog.Backdrop
- Created example project which demonstrates all the headlessui-reagent
  components.
### Fixed
- Worked around a problem where form-1 components couldn't be used as the return
  value of a render-props function. form-2 and form-3 components still won't
  work, though they'll be rare.

## [1.5.0.47]
### Changed
- Upgraded to @headlessui/react 1.5.0
### Added
- Reagent interop for Combobox

## [1.4.3.46]
### Fixed
- cljdocs, by specifying dependency on reagent

## [1.4.3.44]
### Changed
- Upgraded to @headlessui/react 1.4.3

## [1.4.2.43]
### Changed
- Upgraded to @headlessui/react 1.4.2

## [1.4.1.40]
### Changed
- Upgraded to @headlessui/react 1.4.1

## [1.4.0.32]
### Added
- Reagent interop for Tab

### Changed
- Upgraded to @headlessui/react 1.4.0

## [1.2.1]
Update clojars with more repository information.

## [1.2.0]
### Changed:
- Plan to match release tags, or at least major and minor versions to
  @headlessui/react's versions

## [1.1.0]
### Changed:
- Upgraded to @headlessui/react 1.2.0

## [1.0.0] - 2021-04-20
### Changed
- Renamed namespaces:
  `mainej.headlessui-reagent` -> `headlessui-reagent.core`
  `mainej.headlessui-reagent.*` -> `headlessui-reagent.*`

## [0.1.3] - 2021-04-20
### Added
- Reagent interop for Menu, Switch, Dialog, Popover
- Reagent interop for Listbox and RadioGroup, and recommendations for how to
  handle `:value` and `:on-change`.
- Support `:class` as a function. The function will receive the render props.
  Can be useful if the appearance of the component is the only thing that
  depends on the render props.

### Changed
- Fixed pass through of props to :as component. Now all aria attributes, event
  handlers, etc. are passed through. Throw an exception when we suspect that
  this will not happen correctly.

## [0.1.2] - 2021-04-19
### Changed
- Fixed clash between ns and var by removing smaller namespaces. May need to
  revisit, depending on whether the closure compiler can efficiently shake the
  unused components.

## [0.1.1] - 2021-04-19
### Changed
- Fixed definition of focus-trap

## 0.1.0 - 2021-04-19
### Added
- Reagent interop with @headlessui/react for Disclosure, Transition and FocusTrap

[Unreleased]: https://github.com/mainej/headlessui-reagent/compare/v1.6.5.70...main
[1.6.5.70]: https://github.com/mainej/headlessui-reagent/compare/v1.6.4.68...v1.6.5.70
[1.6.4.68]: https://github.com/mainej/headlessui-reagent/compare/v1.6.3.67...v1.6.4.68
[1.6.3.67]: https://github.com/mainej/headlessui-reagent/compare/v1.6.2.64...v1.6.3.67
[1.6.2.64]: https://github.com/mainej/headlessui-reagent/compare/v1.6.1.63...v1.6.2.64
[1.6.1.63]: https://github.com/mainej/headlessui-reagent/compare/v1.6.0.62...v1.6.1.63
[1.6.0.62]: https://github.com/mainej/headlessui-reagent/compare/v1.5.0.47...v1.6.0.62
[1.5.0.47]: https://github.com/mainej/headlessui-reagent/compare/v1.4.3.46...v1.5.0.47
[1.4.3.46]: https://github.com/mainej/headlessui-reagent/compare/v1.4.3.44...v1.4.3.46
[1.4.3.44]: https://github.com/mainej/headlessui-reagent/compare/v1.4.2.43...v1.4.3.44
[1.4.2.43]: https://github.com/mainej/headlessui-reagent/compare/v1.4.1.40...1.4.2.43
[1.4.1.40]: https://github.com/mainej/headlessui-reagent/compare/v1.4.0.32...v1.4.1.40
[1.4.0.32]: https://github.com/mainej/headlessui-reagent/compare/v1.2.1...v1.4.0.32
[1.2.1]: https://github.com/mainej/headlessui-reagent/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/mainej/headlessui-reagent/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/mainej/headlessui-reagent/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/mainej/headlessui-reagent/compare/v0.1.3...v1.0.0
[0.1.3]: https://github.com/mainej/headlessui-reagent/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/mainej/headlessui-reagent/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/mainej/headlessui-reagent/compare/v0.1.0...v0.1.1
