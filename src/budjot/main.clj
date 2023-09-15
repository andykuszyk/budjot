(ns budjot.main
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body]]
            [somnium.congomongo :as m]
            [clojure.string :refer [starts-with?]]
            [taoensso.timbre :as log]
            [clojure.data.json :as json])
  (:gen-class))

(defn insert-jot [jot]
  (let [now (new java.util.Date)]
    (m/insert! :budjot (merge jot {:createdOn now :modifiedOn now}))))

(defn get-jot [id]
  (m/fetch-one :budjot :where {:_id (new org.bson.types.ObjectId id)}))

(defn get-all-jots []
  (m/fetch :budjot :limit 100))

(defn handle-post-jots [request]
  (log/info "handling POST /jots")
  (let [jot (m/fetch-one :budjot :where {:name (:name (:body request))})]
    (if (not= nil jot)
      (do
        (log/info "jot already exists, returning 409")
        {:status 409})
      (do
        (log/info "jot found, returning 201")
        {:status 201 :body (json/write-str (insert-jot (:body request)))}))))

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

(defn list-request? [uri]
  (not= nil (re-find #"/jots[/]*$" uri)))

(defn handle-get-jots [request]
  (log/info "handling GET /jots")
  (if (list-request? (:uri request))
    (do
      (log/info "handling GET /jots with no id (list)")
      {:status 200 :body (json/write-str (get-all-jots))})
    (let [jot-id (get-jot-id (:uri request))]
      (log/info "handling GET /jots for specific ID")
      (if (= nil jot-id)
        (do
          (log/info "jot ID could not be determined, returning 400")
          {:status 400})
        (let [found-jot (get-jot jot-id)]
          (if (= nil found-jot)
            (do
              (log/info "could not find jot, returning 404")
              {:status 404})
            (do
              (log/info "found jot, returning 200")
              {:status 200 :body (json/write-str found-jot)})))))))

(defn handle-get [request]
  (log/info "handling get request")
  (if (starts-with? (:uri request) "/jots")
    (handle-get-jots request)
    (do
      (log/info "uri did not start with /jots, returning 400")
      {:status 400})))

(defn handle-put [request]
  {:status 200 :body (json/write-str {:message "you'll have to put up with this"})})

(defn handle-delete[request]
  {:status 204})

(defn handler [request]
  (log/info "routing http request based on verb")
  (case (:request-method request)
    :get (handle-get request)
    :post (handle-post request)
    :put (handle-put request)
    :delete (handle-delete request)))

(defn start-budjot [join? port mongo-address]
  (m/set-connection! (m/make-connection mongo-address))
  (run-jetty (wrap-json-body handler {:keywords? true}) {:join? join? :port port}))

(defn get-mongo-address []
  (let [addr (System/getenv "BUDJOT_MONGO_ADDR")]
    (if (= (count addr) 0)
      "mongodb://localhost:27017/budjot"
      addr)))

(defn -main
  "Budjot entrypoint"
  [& args]
  (log/info "budjot is starting a web server on port 8080")
  (let [mongo-address (get-mongo-address)]
    (log/info (format "mongo address is %s" mongo-address))
    (start-budjot true 8080 mongo-address)))
