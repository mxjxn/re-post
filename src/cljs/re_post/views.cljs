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

; TODO: MAKE THIS WORK!
(defn login-panel [] 
  (let [usernm (r/atom "")]
    (fn [] 
      [:input {:type "text"
               :on-change #(reset! usernm (-> % .-target .-value))
               :on-key-down #(case (.-which %)
                               13 (re-frame/dispatch [:login @usernm]))}])))

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

; TODO: MAKE THIS WORK!
(defn logged-in-check []
  (let [loggd-in (re-frame/subscribe [:login-status])] 

    (fn []
    (cond
      (= @loggd-in false) [login-panel]
      (= @loggd-in true) [main-panel]))))
