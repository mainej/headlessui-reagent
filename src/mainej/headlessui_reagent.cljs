(ns mainej.headlessui-reagent
  (:require ["@headlessui/react" :as ui]
            [mainej.headlessui-reagent.utils :as utils]))

(def menu (utils/headlessui->reagent ui/Menu))
(def menu-button (utils/headlessui->reagent ui/Menu.Button))
(def menu-items (utils/headlessui->reagent ui/Menu.Items))
(def menu-item (utils/headlessui->reagent ui/Menu.Item))

(def listbox (utils/headlessui->reagent ui/Listbox))
(def listbox-button (utils/headlessui->reagent ui/Listbox.Button))
(def listbox-label (utils/headlessui->reagent ui/Listbox.Label))
(def listbox-options (utils/headlessui->reagent ui/Listbox.Options))
(def listbox-option (utils/headlessui->reagent ui/Listbox.Option))

(def disclosure (utils/headlessui->reagent ui/Disclosure))
(def disclosure-button (utils/headlessui->reagent ui/Disclosure.Button))
(def disclosure-panel (utils/headlessui->reagent ui/Disclosure.Panel))

(defn transition [& args] (into [:> ui/Transition] args))
(defn transition-child [& args] (into [:> ui/Transition.Child] args))

(defn focus-trap [& args] (into [:> ui/FocusTrap] args))
