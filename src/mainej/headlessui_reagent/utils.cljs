(ns mainej.headlessui-reagent.utils
  (:require [reagent.core :as r]))

(defn props-and-children [args]
  (if (or (nil? (first args)) (map? (first args)))
    [(first args) (rest args)]
    [{} args]))

(defn headlessui->reagent
  "Convert a @headlessui/react component into a reagent component."
  [component]
  (fn [& args]
    (let [[props children] (props-and-children args)

          props (if (and (map? props)
                         (contains? props :as)
                         ;; this interop code would handle strings, but better to avoid it
                         (not (string? (:as props))))
                  (update props :as (fn [as]
                                      (reagent.core/reactify-component
                                       (fn [{:keys [children]}]
                                         [as (dissoc props :as) children]))))
                  props)

          children (if (and (= 1 (count children))
                            (fn? (first children)))
                     (let [f (first children)]
                       [(fn [args]
                          (r/as-element [f (js->clj args :keywordize-keys true)]))])
                     children)]
      (into [:> component props] children))))
