(defproject re-post "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.395"]
                 [org.clojure/tools.reader "1.0.0-beta3"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-defaults "0.2.1"]
                 [compojure "1.5.1"]
                 [org.clojure/clojurescript "1.9.293"]
                 [reagent "0.6.0"]
                 [binaryage/devtools "0.8.2"]
                 [cljs-http "0.1.4"]
                 [re-frame "0.8.0"]]
  :plugins [[lein-cljsbuild  "1.1.4"]
            [lein-ring "0.9.7"]
            [lein-pdo "0.1.1"]]
  :source-paths ["src/clj"]
  :profiles {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}}
  :aliases {"up" ["pdo" "cljsbuild" "auto" "dev," "ring" "server-headless"]}
  :ring {:handler re-post.handler/app}
  :cljsbuild 
  {:builds 
   [{:id "dev"
     :source-paths ["src/cljs"]
     :compiler {:main          re-post.core
                :output-to     "resources/public/js/app.js"
                :output-dir    "resources/public/js/out"
                :asset-path    "js/out"
                :optimizations :none
                :source-map    true
                }}
    {:id "min"
     :source-paths ["src/cljs"]
     :compiler {:main            re-post.core
                :output-to       "resources/public/js/app.js"
                :optimizations   :advanced
                :closure-defines {goog.debug false}
                :pretty-print    false}}]})
