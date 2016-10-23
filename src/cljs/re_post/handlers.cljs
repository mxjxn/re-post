(ns re-post.handlers
    (:require-macros [cljs.core.async.macros :refer [go alt!]])
    (:require [re-frame.core :as re-frame :refer [inject-cofx]]
              [re-post.db :as db]
              [cljs.core.async :refer [put! <! >! chan timeout]]
              [cljs-http.client :as http]))

;; Effect handlers
;; * * * * * * * * 

(def posts-url "/posts")
(def post-url "/post")

;; re
(re-frame/reg-fx
  :retrieve-posts
  (fn [value]
    (go (let [resp (<! (http/get posts-url))]
          (re-frame/dispatch [:posts-recieved (:body resp)])))))

(re-frame/reg-fx
  :save-post
  (fn [{:keys [username body]}]
    (js/console.log "save-post: " body)
    (go (let [resp (<! (http/post post-url {:json-params 
                                            {:username username
                                             :body body}}))]
          (re-frame/dispatch [:posts-recieved (:body resp)])))))


(re-frame/reg-event-fx
  :save
  (fn [cofx [_ txt]]
    (assoc cofx :save-post {:username "macks" :body txt})))

(re-frame/reg-event-fx
 :initialize-db
 (fn  [cofx _]
   {:db db/init-db
    :retrieve-posts true}))

(re-frame/reg-event-db
 :posts-recieved
 (fn [app-db posts]
   (assoc app-db :posts (get posts 1))))
