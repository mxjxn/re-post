(ns re-post.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [ring.util.response 
             :refer [resource-response response content-type]]
            [ring.middleware.json :as middleware]
            [ring.middleware.defaults 
             :refer [wrap-defaults api-defaults]]))

(defroutes app-routes
  (GET "/" [] (->
                (resource-response "index.html" {:root "public"})
                (content-type "text/html")))
  (GET "/posts" [] (response [{:username "Max" 
                               :body "This is my first post"}
                              {:username "Max" 
                               :body "This is my second post"}]))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)
      (wrap-defaults api-defaults)))
