(ns re-post.views
    (:require [reagent.core :as r]
              [re-frame.core :as re-frame]))

(defn create-post-panel []
  (let [txt (r/atom "")]
  (fn [] [:div#create-post
   [:input {:on-change #(reset! txt (-> % .-target .-value))
            :type "text" :placeholder "enter a new post"}]
   [:button 
    {:on-click #(re-frame/dispatch [:save @txt])} 
    "Submit"]])))

(defn main-panel []
  (let [posts (re-frame/subscribe [:posts])
        name  (re-frame/subscribe [:name])]
    (fn []
      [:div.content 
       [:h2 "Hello from me," @name]
       [(create-post-panel)]
       [:ul
        (for [post @posts]
          ^{:key(:id post)} 
          [:li 
           [:u "Username: " (:username post)]
           [:p (:body post)]]
          )]])))
