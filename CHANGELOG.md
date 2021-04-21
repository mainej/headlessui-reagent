# Change Log
All notable changes to this project will be documented in this file. This
change log follows the conventions of
[keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]
### Added
- Reagent interop for Menu, Switch, Dialog, Popover
- Reagent interop for Listbox, and recommendations for how to handle `:value`
  and `:on-change`.
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

[Unreleased]: https://github.com/mainej/headlessui-reagent/compare/v0.1.2...main
[0.1.2]: https://github.com/mainej/headlessui-reagent/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/mainej/headlessui-reagent/compare/v0.1.0...v0.1.1
