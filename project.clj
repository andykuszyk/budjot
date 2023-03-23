(defproject budjot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler budjot.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [org.clojure/data.json "2.4.0"]
                        [ring/ring-mock "0.3.2"]]}})
