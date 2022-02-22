(ns headlessui-reagent.utils
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

(defn js->clj-more
  "We need props appropriate for a Reagent component. We receive React style
  props that have been shallowly converted to a CLJ hashmap. Finishes the
  conversion to Reagent props."
  [props]
  (set/rename-keys (reduce (fn [result [k v]]
                             ;; Is js->clj safe?
                             (assoc result (dashify k) (js->clj v)))
                           {}
                           props)
                   {:class-name :class}))

(defn adapt-render-props-fn
  "Adapts a ClojureScript function to work as a Headless UI render props function.

  The ClojureScript function will receive a keyword-ized version of the render
  props and should return a Reagent-style component vector. The first element of
  the vector, the component, can be a hiccup-style keyword or a form-1 Reagent
  component."
  [f]
  (fn [js-slot]
    ;; Call the ClojureScript function, getting back a Reagent vector. Then
    ;; convert that to a React element so that Headless UI can use it.
    (r/as-element
     (let [[comp & args :as comp-vector] (f (js->clj js-slot :keywordize-keys true))]
       ;; HACK: comp is either a hiccup keyword or a Reagent component function.
       ;; Not sure why, but with r/as-element, the keyword works as expected
       ;; while the Reagent component function doesn't propogate changes to the
       ;; js-slot, nor are the id, ARIA attributes or event handlers attached to
       ;; it. To get around this, we call the function, effectively converting
       ;; from the non-working to the working style. Doesn't work when comp is a
       ;; form-2 or form-3 component.
       ;;
       ;; Render props break a lot of React caching, even when used without any
       ;; Reagent interop, so I'm not worried we're making that worse by calling
       ;; the form-1 function.
       (if (fn? comp)
         (let [comp-vector (apply comp args)]
           (assert (vector? comp-vector)
                   "\nThe render props function cannot return a form-2 or form-3 component.\n\nSuggestion: return a hiccup-style keyword or a form-1 component.")
           comp-vector)
         comp-vector)))))

(defn adapt-class-fn
  "Adapts a ClojureScript function to work as a Headless UI class function.

  The ClojureScript function will receive a keyword-ized version of the render
  props and should return whatever usually works as a Reagent :class -- a
  string, keyword, or vector of keywords."
  [f]
  (fn [js-slot]
    (r/class-names (f (js->clj js-slot :keywordize-keys true)))))

(defn adapt-as-component
  "Adapts a ClojureScript :as key to work as a Headless UI 'as' component.

  The :as key can be a hiccup-style keyword or a form-1 Reagent component
  function."
  [as]
  ;; TODO: can this be made to work for react/Fragment or :<> ?
  ;; If so, can it also work for ui/transition, and other components which
  ;; don't pass through headlessui->reagent?
  (r/reactify-component
   (fn [{:keys [children] :as props}]
     [as (js->clj-more (dissoc props :children)) children])))

(defn headlessui->reagent
  "Convert a @headlessui/react component into a reagent component."
  [component]
  (fn [& args]
    (let [[props children] (props-and-children args)

          props (if-not (map? props)
                  props
                  (cond-> props
                    (and (contains? props :as)
                         ;; this interop code would handle strings, but better
                         ;; to let them pass straight through to Headless UI
                         (not (string? (:as props))))
                    (update :as adapt-as-component)

                    (and (contains? props :class)
                         (fn? (:class props)))
                    (update :class adapt-class-fn)))

          children (if (and (= 1 (count children))
                            (fn? (first children)))
                     [(adapt-render-props-fn (first children))]
                     children)]
      (into [:> component props] children))))
