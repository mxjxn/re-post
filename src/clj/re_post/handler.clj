(ns re-post.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [ring.util.response 
             :refer [resource-response response content-type]]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults 
             :refer [wrap-defaults api-defaults]]))

(defn make-user 
  ([uname]
  (make-user uname "password"))
  ([uname pw]
  {:username uname
   :password pw}))

(def mock-users-list #{"max" "steve" "amy" "jenn"})

(def user-db (set (vec (map make-user mock-users-list))))

(def posts (atom []))

(def uid (atom 0))
(defn next-uid [] 
  (swap! uid inc))

(defn create-post! [usr post]
  (if (contains? mock-users-list usr)
    (do 
      (swap! posts conj {:username usr :id (next-uid) :body post}))))

(defroutes app-routes
  (GET "/" [] (->
                (resource-response "index.html" {:root "public"})
                (content-type "text/html")))
  (GET "/posts" [] (response @posts))
  (POST "/post" r (let [req      (:body r)
                        
                        body     (get req "body")
                        username (get req "username")]
                     (create-post! username body)
                     (response @posts)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)
      (wrap-defaults api-defaults)))
