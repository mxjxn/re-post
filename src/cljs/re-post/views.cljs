(ns re-post.views
    (:require [re-frame.core :as re-frame]))

(defn main-panel []
  (let [posts (re-frame/subscribe [:posts])
        name  (re-frame/subscribe [:name])]
    (fn []
      [:div.content 
       [:h2 "Hello from " @name]
       [:ul
        (for [post @posts]
          ^{:key(:id post)} 
          [:li 
           [:u (:username post)]
           [:p (:body post)]]
          )]])))
