(defproject budjot "0.1.0-SNAPSHOT"
  :description "A simple budgeting app"
  :url "https://budjot.com"
  :min-lein-version "2.0.0"
  :main budjot.main
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [ring/ring-jetty-adapter "1.10.0"]
                 [ring/ring-json "0.5.1"]
                 [org.clojure/data.json "2.4.0"]
                 [congomongo "2.6.0"]
                 [com.taoensso/timbre "6.2.2"]
                 [ring/ring-defaults "0.3.2"]]
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [clj-http "3.12.3"]
                        [org.clojure/data.json "2.4.0"]]}})
