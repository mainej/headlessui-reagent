(ns mainej.headlessui-reagent.utils
  (:require [clojure.string :as string]
            [reagent.core :as r]
            [clojure.set :as set]))

(defn props-and-children [args]
  (if (or (nil? (first args)) (map? (first args)))
    [(first args) (rest args)]
    [{} args]))

(defn dashify [kw]
  (->> (string/split (name kw) #"(?=[A-Z])")
       (map string/lower-case)
       (string/join "-")
       keyword))

(defn pass-through-props
  "We need props appropriate for a reagent component. We receive React style
  props that have been shallowly converted to a CLJ hashmap. Finishes the
  conversion to reagent props."
  [props]
  (set/rename-keys (reduce (fn [result [k v]]
                             ;; Is js->clj safe?
                             (assoc result (dashify k) (js->clj v)))
                           {}
                           (dissoc props :children))
                   {:class-name :class}))

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
                                       (fn [{:keys [children] :as inner-props}]
                                         [as (pass-through-props inner-props) children]))))
                  props)

          children (if (and (= 1 (count children))
                            (fn? (first children)))
                     (let [f (first children)]
                       [(fn [args]
                          (r/as-element [f (js->clj args :keywordize-keys true)]))])
                     children)]
      (into [:> component props] children))))
