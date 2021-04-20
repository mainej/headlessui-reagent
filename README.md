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

As with `@headlessui/react`, if the reagent component is given a single
function as a child, the function is called with the headlessui component's
["render props"][render-props] (e.g. :open for a Disclosure). The return
value of the function, which should be a single (hiccup-style) component, will
be rendered.

```clojure
[ui/disclosure
 (fn [{:keys [open]}]
   [:<>
    [ui/disclosure-button (if open "Hide" "Show")]
    [ui/disclosure-panel ,,,]])]
```

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

## License

Copyright © 2021 Jacob Maine

Distributed under the MIT License.

[render-props]: https://reactjs.org/docs/render-props.html
[headlessui]: https://headlessui.dev/
[headlessui-disclosure]: https://headlessui.dev/react/disclosure
