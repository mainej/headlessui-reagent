# headlessui-reagent

Provides reagent wrappers for [@headlessui/react][headlessui] components.

## Installation

Install as a [Clojure dependency][clojars]. Assuming you run your project with
shadow-cljs, `@headlessui/react` will be installed as a JS dependency.
Otherwise, you may have to `npm install` it yourself. `headlessui-reagent` was
built with `@headlessui/react` version `1.2.0` but may work with later versions.

## Usage

Usage follows the headlessui API. For example, to use a
[Disclosure][headlessui-disclosure] in reagent:

```clojure
(require '[headlessui-reagent.core :as ui])

[ui/disclosure
  [ui/disclosure-button {:class [:w-full :px-4 :py-2 :text-sm :font-medium :text-purple-900 :bg-purple-100 :rounded-lg]}
    "Explain"]
  [ui/disclosure-panel {:class [:px-4 :pt-4 :pb-2 :text-sm :text-gray-500]}
    [:p "Some explanation."]]]
```

### Styling the active item

As with `@headlessui/react`, if the reagent component is given a single function
as a child, the function is called with the headlessui component's ["render
props"][render-props] (e.g. `:open` for a Disclosure). The return value of the
function, which should be a single (hiccup-style) component, will be rendered.

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

### Picking an item

The Listbox and RadioGroup components are designed to assist in picking an item.
In both cases, this is coordinated by having a root element and several child
elements. The child elements have a `value`, to identify themselves. The root
element has a `value` matching the currently selected item and an `onChange`
handler that is called with the `value` of a different item when it is selected.

In this library, these correspond to the `:value` and `:on-change` attributes.
However, note that the `:value` and the argument to `:on-change` must be JS
objects. The library does not provide automatic conversion between JS and CLJS.

This begs the question, how do we use these components when the items are
complex CLJS objects? The trick is to ensure that the items each have some
unique identifier that can be cast to a number or string (numbers and strings
are preferred because they are primitives in both CLJS and JS). We use this
unique identifier as the item's `:value`. When a new item is selected,
`:on-change` will be called with the primitive identifier. At that point, we are
back in Clojure code, so we can convert the identifier back into the full item.

In this example, pay attention to how the `:id` is used as the `:value` in both
the `ui/listbox` and `ui/listbox-option` and how it is converted back into a
full person in `:on-change`.

```clojure
(reagent.core/with-let [people [{:id 1, :name "Wade Cooper"}
                                {:id 2, :name "Arlene Mccoy"}
                                {:id 3, :name "Devon Webb"}
                                {:id 4, :name "Tom Cook"}
                                {:id 5, :name "Tanya Fox"}
                                {:id 6, :name "Hellen Schmidt"}]
                        person-by-id (zipmap (map :id people) people)
                        !selected-person (reagent.core/atom (first people))]
  (let [selected-person @!selected-person]
    [ui/listbox
     {:value     (:id selected-person)
      :on-change #(reset! !selected-person (get person-by-id %1))}
     [ui/listbox-button (:name selected-person)]
     [ui/listbox-options
      (for [person people]
        ^{:key (:id person)}
        [ui/listbox-option
         {:value (:id person)}
         (:name person)])]]))
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
[clojars]: https://clojars.org/com.github.mainej/headlessui-reagent
