(ns mainej.headlessui-reagent
  (:require ["@headlessui/react" :as ui]
            [mainej.headlessui-reagent.utils :as utils]))

(def disclosure (utils/headlessui->reagent ui/Disclosure))
(def disclosure-button (utils/headlessui->reagent ui/Disclosure.Button))
(def disclosure-panel (utils/headlessui->reagent ui/Disclosure.Panel))

(defn transition [& args] (into [:> ui/Transition] args))
(defn transition-child [& args] (into [:> ui/Transition.Child] args))

(defn focus-trap [& args] (into [:> ui/FocusTrap] args))
