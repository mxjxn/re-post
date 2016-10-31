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
(def login-url "/login")

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


; TODO: finish this, make it work w mockup data!
(re-frame/reg-fx
  :send-login
  (fn [{:keys [username]}]
    (go (let [response (<! 
                        (http/post login-url 
                         {:json-params {:username username}}))]
          ;TODO: this should parse response and explicitly make a map
          ;      instead of just passing the response body
          (re-frame/dispatch [:logged-in (:body response)])))))


;; Event handlers
;; * * * * * * * *

(re-frame/reg-event-fx
  :save
  (fn [cofx [_ txt]]
    (assoc cofx :save-post {:username (-> cofx :db :name)
                            :body txt})))

(re-frame/reg-event-fx
  :login
  (fn [cofx [_ usrname]]
    (js/console.log "login usrname: " usrname)
    (assoc cofx :send-login {:username usrname})))

(re-frame/reg-event-fx
 :initialize-db
 (fn  [cofx _]
   {:db db/init-db
    :retrieve-posts true}))

(re-frame/reg-event-db
 :posts-recieved
 (fn [app-db [_ posts]]
   (assoc app-db :posts posts )))

(re-frame/reg-event-db
  :logged-in
  (fn [app-db [_ resp]]
    (if-let [success? (:success resp)]
      (assoc app-db :login-status true :name (:username resp))
      (assoc app-db :login-status false))))

