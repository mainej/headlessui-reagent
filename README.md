# headlessui-reagent

Reagent wrappers for [@headlessui/react][headlessui], bringing accessible,
keyboard-friendly, style-agnostic UI components to Reagent and re-frame
projects.

## Installation

Install as a [Clojure dependency][clojars]. Assuming you run your project with
shadow-cljs, `@headlessui/react` will be installed as a JS dependency.
Otherwise, you may have to install it yourself with npm/yarn. 

Since `v1.4.0.32`, `headlessui-reagent` tracks `@headlessui/react`'s versioning.
That is, the first three segments of the version (`1.4.0`) indicate that this
library was built with `@headlessui/react` version `1.4.0`. The last segment
(`32`) distinguishes between releases of this library that were all built with
the same version of `@headlessui/react`.

If for some reason you need an earlier version, both `v1.2.0` and `v1.2.1` of
`headlessui-reagent` were built with `@headlessui/react` `1.2.0`. Earlier
releases were built with `@headlessui/react` `1.0.0`.

## Usage

Usage follows the Headless UI API. For example, to use a
[Disclosure][headlessui-disclosure] in Reagent:

```clojure
(require '[headlessui-reagent.core :as ui])

[ui/disclosure
  [ui/disclosure-button {:class [:w-full :px-4 :py-2 :text-sm :font-medium :text-purple-900 :bg-purple-100 :rounded-lg]}
    "Explain"]
  [ui/disclosure-panel {:class [:px-4 :pt-4 :pb-2 :text-sm :text-gray-500]}
    [:p "Some explanation."]]]
```

To see each of the components in action, check out the [examples](/example).

### Styling the active item

To conditionally apply markup or styles based on the component's state, Headless
UI provides ["render props"][render-props].

As with Headless UI, if the Reagent component is given a single function as a
child, the function is called with a hash map of render props (e.g. `:open` for
a Disclosure). The return value of the function, which should be a single
(hiccup-style) component, will be rendered.

```clojure
[ui/disclosure
 (fn [{:keys [open]}]
   [:<>
    [ui/disclosure-button (if open "Hide" "Show")]
    [ui/disclosure-panel ,,,]])]
```

If you need to control the CSS classes only, not the component's contents,
`:class` can be a function which will receive the render props:

```clojure
[ui/disclosure-button {:class (fn [{:keys [open]}]
                                [:border (when open :bg-blue-200)])}
 "Show more"]
```

### Rendering a different element for a component

Many Headless UI components accept an `"as"` prop, which controls how they are
rendered into the dom. If the corresponding Reagent component is given an `:as`
prop, it can be any hiccup-style component: a string, a keyword or a function
which returns hiccup.

If `:as` is a full-fledged Reagent component (i.e. a function which returns
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
necessary for the `:as` component to work correctly, so you must use them.

The above example is so simple it would be more easily written as:

```clojure
[ui/disclosure-panel {:as :ul.bg-red-500}
  [:li "Note this."]
  [:li "This too."]]
```

Or, closest to the Headless UI style, as:

```clojure
[ui/disclosure-panel {:as "ul", :class [:bg-red-500]}
  [:li "Note this."]
  [:li "This too."]]
```

### Picking an item

The Listbox, RadioGroup and Combobox components are designed to assist in
picking an item from a list of items. Headless UI coordinates this by having a
root element and several child elements. The child elements each have a `value`,
to identify themselves. The root element also has a `value` for the currently
selected item and an `onChange` handler that is called with a different `value`
when another item is selected.

In this library, these correspond to the `:value` and `:on-change` attributes.
The `:value` must be a JavaScript object, and similarly the argument to the
`:on-change` callback will be a JavaScript object. The library does not
automatically convert between JavaScript and ClojureScript.

This begs the question, what's the best way to use these components when the
items are ClojureScript objects? The trick is to ensure that the items each have
a unique identifier that is a JavaScript primitive (numbers and strings are
preferred because they are primitives in both ClojureScript and JavaScript). We
use this unique identifier as the item's `:value`. When a new item is selected,
`:on-change` will be called with the primitive identifier. At that point, we're
back in ClojureScript code, so we can use the identifier to lookup the full
item.

Here's an example. Notice that `:id`, an integer, is used as the `:value` in
both the `ui/listbox` and `ui/listbox-option`. That is, to Headless UI, it's
assisting in picking an integer. That integer is converted back into a full
person in `:on-change`.

```clojure
(def people [{:id 1, :name "Wade Cooper"}
             {:id 2, :name "Arlene Mccoy"}
             {:id 3, :name "Devon Webb"}
             {:id 4, :name "Tom Cook"}
             {:id 5, :name "Tanya Fox"}
             {:id 6, :name "Hellen Schmidt"}])

(def person-by-id (zipmap (map :id people) people))

(reagent.core/with-let [!selected (reagent.core/atom (first people))]
  (let [selected @!selected]
    [ui/listbox
     {:value     (:id selected)
      :on-change #(reset! !selected (get person-by-id %1))}
     [ui/listbox-button (:name selected)]
     [ui/listbox-options
      (for [person people]
        ^{:key (:id person)}
        [ui/listbox-option
         {:value (:id person)}
         (:name person)])]]))
```

## Known bugs

There are some known limitations to the interop between Reagent and Headless UI.
Bug fixes welcome!

### Reagent components and render props

When using the render props style (passing a function as the only argument to a
component), if Headless UI needs to pass props (ARIA attributes, event handlers,
etc.) to the returned component, which it often does, the component must be a
hiccup keyword, not a Reagent component function:

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

This can be annoying, but is necessary because Headless UI can't seem to forward
props to Reagent component function elements in the same way that it can for
hiccup keywords.

### Rendering children directly

In some cases where Headless UI would usually render a wrapper element, it
permits rendering the children directly instead, by passing `React.Fragment` as
`"as"`. This is not supported because Headless UI and Reagent fail to convey
props between them.

```clojure
;; DON'T do this
[ui/menu-button {:as :<>}
  [:button.block {:type "button"} "Open"]]
```

## Acknowledgements

If you'd like to support Tailwind Labs, the creators of Headless UI, consider
signing up for [Tailwind UI][tailwind-ui]. You'll get access to hundreds of UI
components, many of which use Headless UI, and which can be adapted to work with
headlessui-reagent.

headlessui-reagent isn't an official part of Headless UI, and I don't get
anything when you sign up for Tailwind UI. I just believe that Tailwind CSS and
Headless UI are a natural fit for Reagent/Hiccup projects, and want to see them
thrive.

## License

Copyright © 2022 Jacob Maine

Distributed under the MIT License.

[render-props]: https://reactjs.org/docs/render-props.html
[headlessui]: https://headlessui.dev/
[headlessui-disclosure]: https://headlessui.dev/react/disclosure
[clojars]: https://clojars.org/com.github.mainej/headlessui-reagent
[tailwind-ui]: https://tailwindui.com/
