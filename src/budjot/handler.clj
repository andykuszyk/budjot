(ns budjot.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [budjot.budjots :refer [handle-budjot-post]]
            [ring.middleware.json :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defroutes budjot-handler
  (GET "/" [] "Hello World")
  (POST "/jots" request (handle-budjot-post request))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults (wrap-json-response (wrap-json-body budjot-handler {:keywords? true})) api-defaults))
