(ns all-examples
  (:require
   ["@heroicons/react/solid" :as solid]
   ["@heroicons/react/outline" :as outline]
   [clojure.string :as string]
   [headlessui-reagent.core :as ui]
   [reagent.core :as r]
   [reagent.dom :as r.dom]))

(def zero-width-space-props
  {:dangerouslySetInnerHTML {:__html "&#8203;"}})

(def middot-props
  {:dangerouslySetInnerHTML {:__html "&middot;"}})

(def people [{:id 1, :name "Wade Cooper"}
             {:id 2, :name "Arlene Mccoy"}
             {:id 3, :name "Devon Webb"}
             {:id 4, :name "Tom Cook"}
             {:id 5, :name "Tanya Fox"}
             {:id 6, :name "Hellen Schmidt"}])

(def person-by-id (zipmap (map :id people) people))

(defn string->query [s]
  (string/replace (string/lower-case s) #"\s+" ""))

(defn menu-item [icon label]
  [ui/menu-item
   (fn [{:keys [active]}]
     [:button.group.flex.rounded-md.items-center.w-full.px-2.py-2.text-sm
      {:class (if active [:bg-violet-500 :text-white] [:text-gray-900])}
      [:> icon
       {:class [:h-5 :w-5 :mr-2 (if active :text-violet-100 :text-violet-900)]
        :aria-hidden true}]
      label])])

(defn menu-example []
  [:div.w-56.h-72.text-right
   [ui/menu {:as :div.relative.inline-block.text-left}
    [:div
     [ui/menu-button {:class "inline-flex justify-center w-full px-4 py-2 text-sm font-medium text-white bg-black rounded-md bg-opacity-20 hover:bg-opacity-30 focus:outline-none focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75"}
      "Options"
      [:> solid/ChevronDownIcon
       {:class       "w-5 h-5 ml-2 -mr-1 text-violet-200 hover:text-violet-100"
        :aria-hidden true}]]]
    [ui/transition
     {:enter      "transition ease-out duration-100"
      :enter-from "opacity-0 scale-95"
      :enter-to   "opacity-100 scale-100"
      :leave      "transition ease-in duration-75"
      :leave-from "opacity-100 scale-100"
      :leave-to   "opacity-0 scale-95"}
     [ui/menu-items {:class "absolute right-0 w-56 mt-2 origin-top-right bg-white divide-y divide-gray-100 rounded-md shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"}
      [:div.p-1
       [menu-item solid/PencilIcon "Edit"]
       [menu-item solid/DuplicateIcon "Duplicate"]]
      [:div.p-1
       [menu-item solid/ArchiveIcon "Archive"]
       [menu-item solid/DownloadIcon "Download"]]
      [:div.p-1
       [menu-item solid/TrashIcon "Delete"]]]]]])

(defn listbox-example []
  (r/with-let [!selected (r/atom (first people))]
    (let [selected @!selected]
      [:div.w-72.h-72
       [ui/listbox {:value (:id selected)
                    :on-change #(reset! !selected (get person-by-id %))}
        [:div.relative.mt-1
         [ui/listbox-button {:class "relative w-full py-2 pl-3 pr-10 text-left bg-white rounded-lg shadow-md cursor-default focus:outline-none focus-visible:ring-2 focus-visible:ring-opacity-75 focus-visible:ring-white focus-visible:ring-offset-orange-300 focus-visible:ring-offset-2 focus-visible:border-indigo-500 sm:text-sm"}
          [:span.block.truncate (:name selected)]
          [:span.absolute.inset-y-0.right-0.flex.items-center.pr-2.pointer-events-none
           [:> solid/SelectorIcon {:class "w-5 h-5 text-gray-400" :aria-hidden true}]]]
         [ui/transition
          {:leave "transition ease-in duration-100"
           :leave-from "opacity-100"
           :leave-to "opacity-0"}
          [ui/listbox-options
           {:class "absolute w-full py-1 mt-1 overflow-auto text-base bg-white rounded-md shadow-lg max-h-60 ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm"}
           (for [person people]
             ^{:key (:id person)}
             [ui/listbox-option
              {:value (:id person)
               :class (fn [{:keys [active]}]
                        (concat [:cursor-default :select-none :relative :py-2 :pl-10 :pr-4]
                                (if active
                                  [:text-amber-900 :bg-amber-100]
                                  [:text-gray-900])))}
              (fn [{:keys [selected]}]
                [:<>
                 [:span.block.truncate {:class (if selected :font-medium :font-normal)}
                  (:name person)]
                 (when selected
                   [:span.absolute.inset-y-0.left-0.flex.items-center.pl-3.text-amber-600
                    [:> solid/CheckIcon {:class "w-5 h-5" :aria-hidden "true"}]])])])]]]]])))

(defn combobox-example []
  (r/with-let [!selected (r/atom (first people))
               !query (r/atom "")
               !filtered-people (r/reaction
                                 (let [query @!query]
                                   (if (string/blank? query)
                                     people
                                     (filter #(string/includes? (string->query (:name %)) query)
                                             people))))]
    (let [selected        @!selected
          filtered-people @!filtered-people]
      [:div.w-72.h-72
       [ui/combobox {:value     (:id selected)
                     :on-change #(reset! !selected (get person-by-id %))}
        [:div.relative.mt-1
         [:div.relative.w-full.text-left.bg-white.rounded-lg.shadow-md.cursor-default.focus:outline-none.focus-visible:ring-2.focus-visible:ring-opacity-75.focus-visible:ring-white.focus-visible:ring-offset-teal-300.focus-visible:ring-offset-2.sm:text-sm.overflow-hidden
          [ui/combobox-input {:class         "w-full border-none focus:ring-0 py-2 pl-3 pr-10 text-sm leading-5 text-gray-900"
                              :display-value (fn [id] (:name (get person-by-id id)))
                              :on-change     #(reset! !query (string->query (.-value (.-target %))))}]
          [ui/combobox-button {:class "absolute inset-y-0 right-0 flex items-center pr-2"}
           [:> solid/SelectorIcon
            {:class "w-5 h-5 text-gray-400"
             :aria-hidden true}]]]
         [ui/transition
          {:leave      "transition ease-in duration-100"
           :leave-from "opacity-100"
           :leave-to   "opacity-0"}
          [ui/combobox-options {:class "absolute w-full py-1 mt-1 overflow-auto text-base bg-white rounded-md shadow-lg max-h-60 ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm"}
           [:<>
            (for [person filtered-people]
              ^{:key (:id person)}
              [ui/combobox-option
               {:value (:id person)
                :class (fn [{:keys [active]}]
                         (concat [:cursor-default :select-none :relative :py-2 :pl-10 :pr-4]
                                 (if active
                                   [:text-white :bg-teal-600]
                                   [:text-gray-900])))}
               (fn [{:keys [selected active]}]
                 [:<>
                  [:span.block.truncate {:class (if selected :font-medium :font-normal)}
                   (:name person)]
                  (when selected
                    [:span.absolute.inset-y-0.left-0.flex.items-center.pl-3
                     {:class (if active :text-white :text-teal-600)}
                     [:> solid/CheckIcon {:class "w-5 h-5" :aria-hidden "true"}]])])])]]]]]])))

(defn switch-example []
  (r/with-let [!enabled (r/atom false)]
    (let [enabled @!enabled]
      [ui/switch
       {:checked enabled
        :on-change #(swap! !enabled not)
        :class [:relative :inline-flex :flex-shrink-0 :border-2 :border-transparent :rounded-full :cursor-pointer :transition-colors :ease-in-out :duration-200 :focus:outline-none :focus-visible:ring-2 :focus-visible:ring-white :focus-visible:ring-opacity-75 ;; .ease-in-out unneeded since Tailwind CSS 3.0
                (if enabled :bg-teal-900 :bg-teal-700)]
        :style {:height "38px"
                :width "74px"}}
       [:span.sr-only "Use setting"]
       [:span.pointer-events-none.inline-block.rounded-full.bg-white.shadow-lg.transform.ring-0.transition.ease-in-out.duration-200 ;; .transform, .transition, .ease-in-out unneeded since Tailwind CSS 3.0
        {:aria-hidden true
         :class (if enabled :translate-x-9 :translate-x-0)
         :style {:height "34px"
                 :width "34px"}}]])))

(defn question-and-answer [question answer]
  [ui/disclosure {:as :div}
   (fn [{:keys [open]}]
     [:<>
      [ui/disclosure-button
       {:class "flex justify-between w-full px-4 py-2 text-sm font-medium text-left text-purple-900 bg-purple-100 rounded-lg hover:bg-purple-200 focus:outline-none focus-visible:ring focus-visible:ring-purple-500 focus-visible:ring-opacity-75"}
       [:span question]
       [:> solid/ChevronUpIcon {:class (concat [:w-5 :h-5 :text-purple-500]
                                               (when open
                                                 ;; .transform unneeded since Tailwind CSS 3.0
                                                 [:transform :rotate-180]))}]]
      [ui/disclosure-panel {:class "px-4 pt-4 pb-2 text-sm text-gray-500"} answer]])])

(defn disclosure-example []
  [:div.w-full.max-w-md.p-2.mx-auto.bg-white.rounded-2xl.space-y-2
   [question-and-answer
    "What is your refund policy?"
    "If you're unhappy with your purchase for any reason, email us within 90 days and we'll refund you in full, no questions asked."]
   [question-and-answer
    "Do you offer technical support?"
    "No."]])

(defn dialog-example []
  (r/with-let [!open? (r/atom false)
               open #(reset! !open? true)
               close #(reset! !open? false)]
    (let [open? @!open?]
      [:<>
       [:div.flex.items-center.justify-center
        [:button.px-4.py-2.text-sm.font-medium.text-white.bg-black.rounded-md.bg-opacity-20.hover:bg-opacity-30.focus:outline-none.focus-visible:ring-2.focus-visible:ring-white.focus-visible:ring-opacity-75
         {:type "button"
          :on-click open}
         "Open dialog"]]

       [ui/transition
        {;; :appear true ;; appear is irrelevant for us because open? defaults to false, unlike on headlessui.dev
         :show open?}
        [ui/dialog {:on-close close}
         [:div.fixed.inset-0.z-10.overflow-y-auto
          [:div.min-h-screen.px-4.text-center
           ;; NOTE: the structure of this HTML is delicate and has subtle
           ;; interactions to keep the modal centered. The structure we use is
           ;; slightly different from the headlessui.dev example. There, the
           ;; Transition.Child elements are rendered as fragments. Here, since
           ;; we don't support fragments, we move some of the structural styles
           ;; to the transition-child elements, which seems to have the same
           ;; effect.
           [ui/transition-child
            {:enter  "ease-out duration-300"
             :enter-from "opacity-0"
             :enter-to "opacity-100"
             :leave "ease-in duration-200"
             :leave-from "opacity-100"
             :leave-to "opacity-0"}
            [ui/dialog-overlay {:class "fixed inset-0 bg-gray-500 bg-opacity-75"}]]
           ;; Trick browser into centering modal contents.
           ;; This is the "ghost element" technique, described here
           ;; https://css-tricks.com/centering-in-the-unknown/ as well as
           ;; elsewhere.
           [:span.inline-block.h-screen.align-middle
            (assoc zero-width-space-props :aria-hidden true)]
           [ui/transition-child
            ;; .transform isn't needed for the animiation since Tailwind CSS
            ;; 3.0. But, it has the side-effect of creating a stacking context,
            ;; which is necessary. .isolate would be more correct, but we're
            ;; leaving .transform to stay close to headlessui.dev.
            {:class "inline-block align-middle text-left transform"
             :enter  "ease-out duration-300"
             :enter-from "opacity-0 scale-95"
             :enter-to "opacity-100 scale-100"
             :leave "ease-in duration-200"
             :leave-from "opacity-100 scale-100"
             :leave-to "opacity-0 scale-95"}
            ;; NOTE: if your dialog is long and you need to support scrolling
            ;; while the mouse is over the background, wrap this with
            ;; `ui/dialog-panel` and replace `ui/dialog-overlay` with
            ;; `ui/dialog-backdrop`.
            [:div.max-w-md.p-6.my-8.bg-white.shadow-xl.rounded-2xl
             [ui/dialog-title {:as :div.text-lg.font-medium.leading-6.text-gray-900}
              "Payment successful"]
             [:div.mt-2
              [:p.text-sm.text-gray-500 "Your payment has been successfully submitted. We’ve sent you an email with all of the details of your order."]]
             [:div.mt-4
              [:button.inline-flex.justify-center.px-4.py-2.text-sm.font-medium.text-blue-900.bg-blue-100.border.border-transparent.rounded-md.hover:bg-blue-200.focus:outline-none.focus-visible:ring-2.focus-visible:ring-offset-2.focus-visible:ring-blue-500
               {:type "button"
                :on-click close}
               "Got it, thanks!"]]]]]]]]])))

(def solutions
  [{:name "Insights"
    :description "Measure actions your users take"
    :href "##"
    :icon outline/LightBulbIcon}
   {:name "Automations"
    :description "Create your own targeted content"
    :href "##"
    :icon outline/CogIcon}
   {:name "Reports"
    :description "Keep track of your growth"
    :href "##"
    :icon outline/ChartBarIcon}])

(defn popover-example []
  [:div.w-full.max-w-sm.px-4
   {:style {:height "400px"}}
   [ui/popover {:class :relative}
    (fn [{:keys [open]}]
      [:<>
       ;; text-opacity-* is not a Tailwind CSS class. This is probably a bug in headlessui.dev. Preserving anyway.
       [ui/popover-button {:class [:text-white :group :bg-orange-700 :px-3 :py-2 :rounded-md :inline-flex :items-center :text-base :font-medium :hover:text-opacity-100 :focus:outline-none :focus-visible:ring-2 :focus-visible:ring-white :focus-visible:ring-opacity-75
                                   (when open :text-opacity-90)]}
        [:span "Solutions"]
        [:> solid/ChevronDownIcon
         {:class [:ml-2 :h-5 :w-5 :text-orange-300 :group-hover:text-opacity-80 :transition :ease-in-out :duration-150 ;; .ease-in-out, .duration-150 unneeded since Tailwind CSS 3.0
                  (when open :text-opacity-70)]
          :aria-hidden true}]]
       [ui/transition
        {:enter "transition ease-out duration-200"
         :enter-from "opacity-0 translate-y-1"
         :enter-to "opacity-100 translate-y-0"
         :leave "transition ease-in duration-150"
         :leave-from "opacity-100 translate-y-0"
         :leave-to "opacity-0 translate-y-1"}
        [ui/popover-panel
         {:class "absolute z-10 w-screen max-w-sm px-4 mt-3 transform -translate-x-1/2 left-1/2 sm:px-0 lg:max-w-3xl"} ;; .transform unneeded since Tailwind CSS 3.0
         [:div.overflow-hidden.rounded-lg.shadow-lg.ring-1.ring-black.ring-opacity-5
          [:div.relative.grid.gap-8.bg-white.p-7.lg:grid-cols-2
           (for [{:keys [name description href icon]} solutions]
             ^{:key name}
             [:a.flex.items-center.p-2.-m-3.transition.duration-150.ease-in-out.rounded-lg.hover:bg-gray-50.focus:outline-none.focus-visible:ring.focus-visible:ring-orange-500.focus-visible:ring-opacity-50 ;; .ease-in-out, .duration-150 unneeded since Tailwind CSS 3.0
              {:href href}
              [:div.flex.items-center.justify-center.flex-shrink-0.text-white
               [:div.w-10.h-10.sm:h-12.sm:w-12.rounded-md.bg-orange-100.flex.items-center.justify-center
                [:> icon {:class [:text-orange-800 :w-8 :h-8 :sm:w-10 :sm:h-10] :aria-hidden true}]]]
              [:div.ml-4
               [:p.text-sm.font-medium.text-gray-900 name]
               [:p.text-sm.text-gray-500 description]]])]
          [:div.p-4.bg-gray-50
           [:a.flow-root.px-2.py-2.transition.duration-150.ease-in-out.rounded-md.hover:bg-gray-100.focus:outline-none.focus-visible:ring.focus-visible:ring-orange-500.focus-visible:ring-opacity-50 ;; .ease-in-out, .duration-150 unneeded since Tailwind CSS 3.0
            {:href "##"}
            [:span.flex.items-center
             [:span.text-sm.font-medium.text-gray-900 "Documentation"]]
            [:span.block.text-sm.text-gray-500 "Start integrating products and tools"]]]]]]])]])

(def plans
  [{:name "Startup"
    :ram  "12GB"
    :cpus "6 CPUs"
    :disk "160 GB SSD disk"}
   {:name  "Business"
    :ram  "16GB"
    :cpus "8 CPUs"
    :disk "512 GB SSD disk"}
   {:name  "Enterprise"
    :ram  "32GB"
    :cpus "12 CPUs"
    :disk "1024 GB SSD disk"}])

(def plans-by-name
  (zipmap (map :name plans) plans))

(defn radio-group-example []
  (r/with-let [!selected (r/atom (first plans))]
    (let [selected @!selected]
      [:div.w-full.px-4
       [:div.w-full.max-w-md.mx-auto
        [ui/radio-group
         {:value (:name selected)
          :on-change #(reset! !selected (get plans-by-name %))}
         [ui/radio-group-label {:class :sr-only} "Server size"]
         [:div.space-y-2
          (for [plan plans]
            ^{:key (:name plan)}
            [ui/radio-group-option
             {:value (:name plan)
              :class (fn [{:keys [active checked]}]
                       (concat [:relative :rounded-lg :shadow-md :px-5 :py-4 :cursor-pointer :flex :focus:outline-none]
                               (when active
                                 [:ring-2 :ring-offset-2 :ring-offset-sky-300 :ring-white :ring-opacity-60])
                               (if checked
                                 [:bg-sky-900 :bg-opacity-75 :text-white]
                                 [:bg-white])))}
             (fn [{:keys [active checked]}]
               [:div.flex.items-center.justify-between.w-full
                [:div.flex.items-center
                 [:div.text-sm
                  [ui/radio-group-label {:as :p.font-medium :class (if checked :text-white :text-gray-900)}
                   (:name plan)]
                  [ui/radio-group-description {:as :span.inline :class (if checked :text-sky-100 :text-gray-500)}
                   [:span (:ram plan) "/" (:cpus plan)] " "
                   [:span (assoc middot-props :aria-hidden true)] " "
                   [:span (:disk plan)]]]]
                (when checked
                  [:div.flex.shrink-0.text-white
                   [:> solid/CheckCircleIcon {:class [:w-6 :h-6]}]])])])]]]])))

(def categories
  [{:name "Recent"
    :posts [{:id 1
             :title "Does drinking coffee make you smarter?"
             :date "5h ago"
             :comment-count 5
             :share-count 2}
            {:id 2
             :title "So you've bought coffee... now what?"
             :date "2h ago"
             :comment-count 3
             :share-count 2}]}
   {:name "Popular"
    :posts [{:id 1
             :title "Is tech making coffee better or worse?"
             :date "Jan 7"
             :comment-count 29
             :share-count 16}
            {:id 2
             :title "The most innovative things happening in coffee"
             :date "Mar 19"
             :comment-count 24
             :share-count 12}]}
   {:name "Trending"
    :posts [{:id 1
             :title "Ask Me Anything: 10 answers to your questions about coffee"
             :date "2d ago"
             :comment-count 9
             :share-count 5}
            {:id 2
             :title "The worst advice we've ever heard about coffee"
             :date "4d ago"
             :comment-count 1
             :share-count 2}]}])

(defn tabs-example []
  [:div.w-full.max-w-md.px-2.sm:px-0
   [ui/tab-group
    [ui/tab-list {:class "flex p-1 space-x-1 bg-blue-900/20 rounded-xl"}
     (for [category (map :name categories)]
       ^{:key category}
       [ui/tab {:class (fn [{:keys [selected]}]
                         (concat [:w-full :py-3 :text-sm :leading-5 :font-medium :text-blue-700 :rounded-lg]
                                 [:focus:outline-none :focus:ring-2 :ring-offset-2 :ring-offset-blue-400 :ring-white :ring-opacity-60]
                                 (if selected
                                   [:bg-white :shadow]
                                   [:text-blue-100 "hover:bg-white/10" :hover:text-white])))}
        category])]
    [ui/tab-panels {:class :mt-2}
     (for [{:keys [posts name]} categories]
       ^{:key name}
       [ui/tab-panel {:class (concat [:bg-white :rounded-xl :p-3]
                                     [:focus:outline-none :focus:ring-2 :ring-offset-2 :ring-offset-blue-400 :ring-white :ring-opacity-60])}
        [:ul
         (for [{:keys [id title date comment-count share-count]} posts]
           ^{:key id}
           [:li.relative.p-3.rounded-md.hover:bg-gray-200
            [:h3.text-sm.font-medium.leading-5.text-black
             title]
            [:ul.flex.mt-1.space-x-1.text-xs.font-normal.leading-4.text-gray-500
             [:li date]
             [:li middot-props]
             [:li comment-count " comments"]
             [:li middot-props]
             [:li share-count " shares"]]
            [:a.absolute.inset-0.rounded-md.focus:z-10.focus:outline-none.focus:ring-2.ring-blue-400
             {:href "##"}]])]])]]])

(defn example [title opts child]
  [:div.relative.mt-8.mb-12.sm:mx-10.p-8.sm:rounded-xl.overflow-hidden.space-y-8.bg-gradient-to-r
   opts
   [:h2.text-white.opacity-80.text-5xl.font-bold title]
   [:div.flex.flex-col.items-center.w-full
    child]])

(defn main-panel []
  [:main.container.mx-auto
   [example "Menu (Dropdown)"
    {:class "from-purple-500 to-indigo-500"}
    [menu-example]]
   [example "Listbox (Select)"
    {:class "from-amber-300 to-orange-500"}
    [listbox-example]]
   [example "Combobox (Autocomplete)"
    {:class "from-teal-400 to-cyan-400"}
    [combobox-example]]
   [example "Switch (toggle)"
    {:class "from-green-400 to-cyan-500"}
    [switch-example]]
   [example "Disclosure"
    {:class "from-fuchsia-500 to-purple-600"}
    [disclosure-example]]
   [example "Dialog (Modal)"
    {:class "from-sky-400 to-indigo-500"}
    [dialog-example]]
   [example "Popover"
    {:class "from-orange-400 to-pink-600"}
    [popover-example]]
   [example "Radio Group"
    {:class "from-cyan-400 to-sky-500"}
    [radio-group-example]]
   [example "Tabs"
    {:class "from-sky-400 to-blue-600"}
    [tabs-example]]])

(defn ^:after-load mount-root []
  (r.dom/render [main-panel]
                (.getElementById js/document "app")))

(defn ^:export init []
  (mount-root))
