(ns budjot.users
  (:require [clojure.data.json :as json]
            [taoensso.timbre :as log]
            [budjot.storage :as storage]))


(defn handle-post [request]
  (let [id (get (:headers request) "authorization")] ; TODO the id should be taken from the auth header
    (log/info "handling POST /users")
    (if (storage/get-user-by-id id)
      (do
        (log/info "user already exists, returning 204")
        {:status 204})
      (do
        (log/info "user doesn't exist, creating it")
        (storage/insert-user id)
        {:status 201}))))
