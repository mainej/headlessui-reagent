(ns mainej.headlessui-reagent.transition
  (:require ["@headlessui/react" :refer (Transition Transition.Child)]))

(defn transition [& args] (into [:> Transition] args))
(defn transition-child [& args] (into [:> Transition.Child] args))
