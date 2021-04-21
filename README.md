# headlessui-reagent

Provides reagent wrappers for [@headlessui/react][headlessui] components.

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
are preferred because they are primitives in both CLJS and JS). This unique
identifier is used as the item's `:value`. When a new item is selected,
`:on-change` will be called with the primitive identifier. At that point, we are
back in Clojure code, so we can convert the identifier back into the full item.

In this example, pay attention to how the `:id` is used as the `:value` in the
`ui/listbox` and `ui/listbox-option` and how it is converted back into a full
person in `:on-change`.

```clojure
(reagent.core/with-let [people [{ :id 1, :name "Wade Cooper" }
                                { :id 2, :name "Arlene Mccoy" }
                                { :id 3, :name "Devon Webb" }
                                { :id 4, :name "Tom Cook" }
                                { :id 5, :name "Tanya Fox" }
                                { :id 6, :name "Hellen Schmidt" }]
                        person-by-id (zipmap (map :id people) people)
                        !selected-person (reagent.core/atom (first people))]
  (let [person @!selected-person]
    [:div.w-72
     [ui/listbox
      {:on-change #(reset! !selected-person (get person-by-id %1)), :value (:id person)}
      (fn [{:keys [open]}]
        [:div.relative.mt-1
         [ui/listbox-button {:class [:relative :w-full :py-2 :pl-3 :pr-10 :text-left :bg-white :rounded-lg :shadow-md :cursor-default :focus:outline-none :focus-visible:ring-2 :focus-visible:ring-opacity-75 :focus-visible:ring-white :focus-visible:ring-offset-orange-300 :focus-visible:ring-offset-2 :focus-visible:border-indigo-500 :sm:text-sm]}
          [:span.block.truncate (:name person)]]
         [ui/transition
          {:leave-to   "opacity-0",
           :leave-from "opacity-100",
           :leave      "transition ease-in duration-100",
           :show       open}
          [ui/listbox-options
           {:class [:absolute :w-full :py-1 :mt-1 :overflow-auto :text-base :bg-white :rounded-md :shadow-lg :max-h-60 :ring-1 :ring-black :ring-opacity-5 :focus:outline-none :sm:text-sm]}
           (for [person people]
             [ui/listbox-option
              {:as    :li.cursor-default.select-none.relative.py-2.pl-10.pr-4
               :class #(if (:active %1) [:text-yellow-900 :bg-yellow-100] :text-gray-900)
               :key   (:id person)
               :value (:id person)}
              (fn [{:keys [selected]}]
                [:<>
                 [:span.block.truncate {:class (if selected :font-medium :font-normal)}
                  (:name person)]
                 (when selected
                   [:span.absolute.inset-y-0.left-0.flex.items-center.pl-3.text-yellow-600
                    [svg.solid/x {:aria-hidden "true"}]])])])]]])]]))
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
