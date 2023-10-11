(ns budjot.jots
  (:require [budjot.storage :as storage]
            [clojure.data.json :as json]
            [clojure.spec.alpha :as s]
            [taoensso.timbre :as log]))

(s/def :jots/post-budjot (s/keys :req-un [::name ::income ::userid ::entries]))

(s/def :jots/get-budjot (s/keys :req-un [::name ::income ::createdOn ::modifiedOn ::entries ::id]))

(defn sanitise-id [jot]
  (dissoc (merge jot {:id (:_id jot)}) :_id))

(defn handle-post [request]
  (log/info "handling POST /jots")
  (if (s/valid? :jots/post-budjot (:body request))
    (let [jot (storage/get-jot-by-name (:name (:body request)))]
      (if (not= nil jot)
        (do
          (log/info "jot already exists, returning 409")
          {:status 409})
        (do
          (log/info "inserting jot")
          (let [inserted-jot (sanitise-id (storage/insert-jot (:body request)))]
            (if (s/valid? :jots/get-budjot inserted-jot)
              (do
                (log/info "jot inserted, returning 201")
                {:status 201 :body (json/write-str inserted-jot)})
              (do
                (log/info
                 {:message "the data returned from the database was invalid, returning 500" :data inserted-jot})
                {:status 500 :body "the data returned from the database was invalid"}))))))
    (do
      (log/info "request body does not confirm to budjot spec, returning 400")
      {:status 400})))

(defn get-jot-id [uri]
  (let [matches (re-matches #"/jots/(.*)" uri)]
    (if (not= 2 (count matches))
      nil
      (get matches 1))))

(defn- list-request? [uri]
  (not= nil (re-find #"/jots[/]*$" uri)))

(defn handle-get [request]
  (log/info "handling GET /jots")
  (if (list-request? (:uri request))
    (do
      (log/info "handling GET /jots with no id (list)")
      {:status 200 :body (json/write-str (storage/get-all-jots))})
    (let [jot-id (get-jot-id (:uri request))]
      (log/info "handling GET /jots for specific ID")
      (if (= nil jot-id)
        (do
          (log/info "jot ID could not be determined, returning 400")
          {:status 400})
        (let [found-jot (storage/get-jot-by-id jot-id)]
          (if (= nil found-jot)
            (do
              (log/info "could not find jot, returning 404")
              {:status 404})
            (do
              (log/info "found jot, returning 200")
              {:status 200 :body (json/write-str found-jot)})))))))
