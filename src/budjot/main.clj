(ns budjot.main
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body]]
            [somnium.congomongo :as m]
            [clojure.string :refer [starts-with?]]
            [clojure.data.json :as json])
  (:gen-class))

(defn insert-jot [jot]
  (let [now (new java.util.Date)]
    (m/insert! :budjot (merge jot {:createdOn now :modifiedOn now}))))

(defn get-jot [id]
  (m/fetch-one :budjot :where {:_id (new org.bson.types.ObjectId id)}))

(defn get-all-jots []
  (m/fetch :budjot :limit 100))

(defn return-post-conflict []
  (println "jot already exists, returning 409")
  {:status 409})

(defn handle-post-jots [request]
  (println "handling POST /jots")
  (let [jot (m/fetch-one :budjot :where {:name (:name (:body request))})]
    (if (not= nil jot)
      (return-post-conflict)
      {:status 201 :body (json/write-str (insert-jot (:body request)))})))

(defn handle-post-users [request]
  {:status 201
   :body (json/write-str (:body request))})

(defn handle-post [request]
  (case (:uri request)
    "/jots" (handle-post-jots request)
    "/users" (handle-post-users request)
    {:status 400}))

(defn get-jot-id [uri]
  (let [matches (re-matches #"/jots/(.*)" uri)]
    (if (not= 2 (count matches))
      nil
      (get matches 1))))

(defn is-list-request? [uri]
  (not= nil (re-find #"/jots[/]*$" uri)))

(defn handle-get-all-jots []
  (println "handling GET /jots with no id (list)")
  {:status 200 :body (json/write-str (get-all-jots))})

(defn could-not-find-jot []
  (println "could not find jot, returning 404")
  {:status 404})

(defn could-not-determine-jot-id []
  (println "jot ID could not be determined, returning 400")
  {:status 400})

(defn return-found-jot [found-jot]
  (println "found jot, returning 200")
  {:status 200 :body (json/write-str found-jot)})

(defn handle-get-jots [request]
  (println "handling GET /jots")
  (if (is-list-request? (:uri request))
    (handle-get-all-jots)
    (let [jot-id (get-jot-id (:uri request))]
      (println "handling GET /jots for specific ID")
      (if (= nil jot-id)
        (could-not-determine-jot-id)
        (let [found-jot (get-jot jot-id)]
          (if (= nil found-jot)
            (could-not-find-jot)
            (return-found-jot found-jot)))))))

(defn return-bad-get-uri []
  (println "uri did not start with /jots, returning 400")
  {:status 400})

(defn handle-get [request]
  (println "handling get request")
  (if (starts-with? (:uri request) "/jots")
    (handle-get-jots request)
    (return-bad-get-uri)))

(defn handle-put [request]
  {:status 200 :body (json/write-str {:message "you'll have to put up with this"})})

(defn handle-delete[request]
  {:status 204})

(defn handler [request]
  (println "routing http request based on verb")
  (case (:request-method request)
    :get (handle-get request)
    :post (handle-post request)
    :put (handle-put request)
    :delete (handle-delete request)))

(defn start-budjot [join? port mongo-address]
  (m/set-connection! (m/make-connection mongo-address))
  (run-jetty (wrap-json-body handler {:keywords? true}) {:join? join? :port port}))

(defn -main
  "Budjot entrypoint"
  [& args]
  (.println System/out "budjot is starting a web server on port 8080")
  (start-budjot true 8080 "mongodb://localhost:27017/budjot"))
