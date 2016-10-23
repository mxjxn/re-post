(ns re-post.handler
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [ring.util.response 
             :refer [resource-response response content-type]]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults 
             :refer [wrap-defaults api-defaults]]))

(def posts (atom [{:username "Max" 
                   :id       "post-1"
                   :body     "This is my first post"}
                 ,{:username "Max" 
                   :id       "post-2"
                   :body     "This is my second post"}]))

(defroutes app-routes
  (GET "/" [] (->
                (resource-response "index.html" {:root "public"})
                (content-type "text/html")))
  (POST "/post" r (let [req      (:body r)
                        
                        body     (get req "body")
                        username (get req "username")]
                       (println "req: " req)
                       (println "username: " username)
                       (println "body: " body)
                       (swap! posts conj {:username username, :body body})
                       (response @posts)))
  (GET "/posts" [] (response @posts))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)
      (wrap-defaults api-defaults)))
