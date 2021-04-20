# headlessui-reagent

Provides reagent wrappers for [@headlessui/react][headlessui] components.

## Usage

Usage follows the headlessui API. For example, to use a
[Disclosure][headlessui-disclosure] in reagent:

```clojure
(require '[mainej.headlessui-reagent :as ui])
```

```clojure
[ui/disclosure
  [ui/disclosure-button {:class [:w-full :px-4 :py-2 :text-sm :font-medium :text-purple-900 :bg-purple-100 :rounded-lg]}
    "Explain"]
  [ui/disclosure-panel {:class [:px-4 :pt-4 :pb-2 :text-sm :text-gray-500]}
    [:p "Some explanation."]]]
```

### Styling the active item

As with `@headlessui/react`, if the reagent component is given a single
function as a child, the function is called with the headlessui component's
["render props"][render-props] (e.g. :open for a Disclosure). The return
value of the function, which should be a single (hiccup-style) component, will
be rendered.

This can be used to conditionally apply markup or styles based on the
component's state.

```clojure
[ui/disclosure
 (fn [{:keys [open]}]
   [:<>
    [ui/disclosure-button (if open "Hide" "Show")]
    [ui/disclosure-panel ,,,]])]
```

If you only need to control the classes based on the active state, the `:class`
prop can be a function which will receive the render props:

```clojure
[ui/disclosure-button {:class (fn [{:keys [open]}]
                                [:border (when open :bg-blue-200)])}
 "Show more"]
```

### Rendering a different element for a component

Many headlessui components accept an `"as"` prop, which controls how they are
rendered into the dom. If the corresponding reagent component is given an `:as`
prop, it can be any hiccup-style component: a string, a keyword or a function
which returns hiccup.

If `:as` is a full-fledged reagent component (i.e. a function which returns
hiccup), then that component must accept two arguments, its properties and its
children:

```clojure
(defn panel-ul [props children]
  (into [:ul.bg-red-500 props] children))

[ui/disclosure-panel {:as panel-ul}
  [:li "Note this."]
  [:li "This too."]]
```

The props will contain ARIA attributes, event handlers and other attributes
necessary for the :as component to work correctly, so you must use them.

The above example is so simple it would be more easily written as:

```clojure
[ui/disclosure-panel {:as :ul.bg-red-500}
  [:li "Note this."]
  [:li "This too."]]
```

Or, closest to the headlessui style, as:

```clojure
[ui/disclosure-panel {:as "ul", :class [:bg-red-500]}
  [:li "Note this."]
  [:li "This too."]]
```

## Known bugs

There are some known limitations to the interop between reagent and headlessui.
Bug fixes welcome!

### Reagent components and render props

When using the render props style (passing a function as the only argument to a
component), if headlessui needs to pass props (ARIA attributes, event handlers,
etc.) to the returned component, which it often does, the component must be a
hiccup keyword, not a reagent component function:

```clojure
;; DON'T do this
(defn my-component [{:keys [active]} copy]
  [:a.block {:href "#" :class (when active :bg-blue-500)} copy])

[ui/menu-item
  (fn [props]
    [my-component props "A menu item"])]

;; Instead, do this
[ui/menu-item
  (fn [{:keys [active]}]
    [:a.block {:href "#" :class (when active :bg-blue-500)} "A menu item"])]
```

This can be annoying, but is necessary because headlessui can't seem to forward
props to reagent component function elements in the same way that it can for
hiccup keywords.

### Rendering children directly

In some cases where headlessui would usually render a wrapper element, it
permits rendering the children directly instead, by passing `React.Fragment` as
`"as"`. This is not supported because headlessui and reagent fail to convey
props between them.

```clojure
;; DON'T do this
[ui/menu-button {:as :<>}
  [:button.block {:type "button"} "Open"]]
```

## License

Copyright © 2021 Jacob Maine

Distributed under the MIT License.

[render-props]: https://reactjs.org/docs/render-props.html
[headlessui]: https://headlessui.dev/
[headlessui-disclosure]: https://headlessui.dev/react/disclosure
