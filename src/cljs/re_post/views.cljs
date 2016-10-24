(ns re-post.views
    (:require [reagent.core :as r]
              [re-frame.core :as re-frame]))

(defn create-post-panel [name]
  (let [txt (r/atom "")]
  (fn [] 
    [:div#create-post.navbar
      [:div.navbar-header [:a.navbar-brand @name]]
      [:div.container-fluid
       [:div.navbar-collapse
         [:div.navbar-form.navbar-left 
          [:div.form-group [:input.form-control {:on-change #(reset! txt (-> % .-target .-value))
                  :type "text" :placeholder "enter a new post"}]]
         [:button.btn.btn-default 
          {:on-click #(re-frame/dispatch [:save @txt])} 
          "Submit"]]]]])))

(defn main-panel []
  (let [posts (re-frame/subscribe [:posts])
        name  (re-frame/subscribe [:name])]
    (fn []
      [:div.content
       [(create-post-panel name)]
       [:ul
        (for [post @posts]
          ^{:key(:id post)} 
          [:li 
           [:u "Username: " (:username post)]
           [:p (:body post)]])]])))
