(ns budjot.static
  (:require
   [taoensso.timbre :as log]
   [ring.middleware.resource :as resource]))

(defn handle-get [request]
  (log/info "received a request for static assets")
  (resource/wrap-resource get "app"))

(defn- get [request]
  {:status 200})
