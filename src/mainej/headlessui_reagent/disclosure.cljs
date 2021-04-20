(ns mainej.headlessui-reagent.disclosure
  (:require ["@headlessui/react" :refer (Disclosure Disclosure.Button Disclosure.Panel)]
            [mainej.headlessui-reagent.utils :as utils]))

(def disclosure (utils/headlessui->reagent Disclosure))
(def disclosure-button (utils/headlessui->reagent Disclosure.Button))
(def disclosure-panel (utils/headlessui->reagent Disclosure.Panel))
