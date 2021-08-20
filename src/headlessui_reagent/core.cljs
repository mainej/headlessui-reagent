(ns headlessui-reagent.core
  (:require ["@headlessui/react" :as ui]
            [reagent.core :as r]
            [headlessui-reagent.utils :as utils]))

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

(def radio-group (utils/headlessui->reagent ui/RadioGroup))
(def radio-group-option (utils/headlessui->reagent ui/RadioGroup.Option))
(def radio-group-label (utils/headlessui->reagent ui/RadioGroup.Label))
(def radio-group-description (utils/headlessui->reagent ui/RadioGroup.Description))

(def tab-group (utils/headlessui->reagent ui/Tab.Group))
(def tab-list (utils/headlessui->reagent ui/Tab.List))
(def tab (utils/headlessui->reagent ui/Tab))
(def tab-panels (utils/headlessui->reagent ui/Tab.Panels))
(def tab-panel (utils/headlessui->reagent ui/Tab.Panel))

(def transition (r/adapt-react-class ui/Transition))
(def transition-child (r/adapt-react-class ui/Transition.Child))

(def focus-trap (r/adapt-react-class ui/FocusTrap))
