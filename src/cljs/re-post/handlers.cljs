(ns re-post.handlers
    (:require-macros [cljs.core.async.macros :refer [go alt!]])
    (:require [re-frame.core :as re-frame :refer [inject-cofx]]
              [re-post.db :as db]
              [cljs.core.async :refer [put! <! >! chan timeout]]
              [cljs-http.client :as http]))

(def posts-url "/posts")

(defn fetch-posts []
  (go (let [resp (<! (http/get posts-url))]
    (re-frame/dispatch [:posts-recieved (:body resp)]))))
    
  

(re-frame/reg-fx
  :retrieve-posts
  (fn [app-db event-vec]
    (fetch-posts)
    {:db app-db}))

(re-frame/reg-event-fx
 :initialize-db
 (fn  [cofx _]
   {:db db/init-db
    :retrieve-posts true}))

(re-frame/reg-event-db
 :posts-recieved
 (fn [app-db posts]
   (.log js/console "posts recieved: " (-> posts (get 1) (get 0) :username))
   (assoc app-db :posts (get posts 1))))
