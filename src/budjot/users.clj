(ns budjot.users
  (:require [clojure.data.json :as json]))

(defn handle-post [request]
  {:status 201
   :body (json/write-str (:body request))})
