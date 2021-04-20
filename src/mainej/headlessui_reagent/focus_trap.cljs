(ns mainej.headlessui-reagent.focus-trap
  (:require ["@headlessui/react" :refer (FocusTrap)]))

(defn focus-trap [& args] (into [:> FocusTrap] args))
