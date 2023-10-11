(ns budjot.storage
  (:require [somnium.congomongo :as mongo]))

(defn insert-jot [jot]
  (let [now (new java.util.Date)]
    (mongo/insert! :budjot (merge jot {:createdOn now :modifiedOn now}))))

(defn get-jot-by-id [id]
  (mongo/fetch-one :budjot :where {:_id (new org.bson.types.ObjectId id)}))

(defn get-all-jots []
  (mongo/fetch :budjot :limit 100))

(defn get-jot-by-name [name]
  (mongo/fetch-one :budjot :where {:name name}))

(defn delete-jot-by-id [id]
  (mongo/destroy! :budjot {:_id (new org.bson.types.ObjectId id)}))

(defn update-jot [id jot]
  (mongo/update! :budjot
                 {:_id (new org.bson.types.ObjectId id)}
                 {:$set {:name (:name jot)}}))
