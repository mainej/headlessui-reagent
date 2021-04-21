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

(def switch (utils/headlessui->reagent ui/Switch))
(def switch-label (utils/headlessui->reagent ui/Switch.Label))
(def switch-description (utils/headlessui->reagent ui/Switch.Description))
(def switch-group (utils/headlessui->reagent ui/Switch.Group))

(def disclosure (utils/headlessui->reagent ui/Disclosure))
(def disclosure-button (utils/headlessui->reagent ui/Disclosure.Button))
(def disclosure-panel (utils/headlessui->reagent ui/Disclosure.Panel))

(def dialog (utils/headlessui->reagent ui/Dialog))
(def dialog-overlay (utils/headlessui->reagent ui/Dialog.Overlay))
(def dialog-title (utils/headlessui->reagent ui/Dialog.Title))
(def dialog-description (utils/headlessui->reagent ui/Dialog.Description))

(def popover (utils/headlessui->reagent ui/Popover))
(def popover-overlay (utils/headlessui->reagent ui/Popover.Overlay))
(def popover-button (utils/headlessui->reagent ui/Popover.Button))
(def popover-panel (utils/headlessui->reagent ui/Popover.Panel))
(def popover-group (utils/headlessui->reagent ui/Popover.Group))

(defn transition [& args] (into [:> ui/Transition] args))
(defn transition-child [& args] (into [:> ui/Transition.Child] args))

(defn focus-trap [& args] (into [:> ui/FocusTrap] args))
