(ns budjot.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [budjot.budjots :refer [handle-budjot-post]]
            [ring.middleware.json :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defroutes budjot-handler
  (GET "/" [] "Hello World")
  (wrap-json-body (POST "/jots" request (handle-budjot-post request) {:keywords? true}))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults budjot-handler api-defaults))
