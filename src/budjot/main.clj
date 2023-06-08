(ns budjot.main
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-body]]
            [somnium.congomongo :as m]
            [clojure.data.json :as json])
  (:gen-class))

(defn insert-jot [jot]
  (let [now (new java.util.Date)]
    (m/insert! :budjot (merge jot {:createdOn now :modifiedOn now}))))

(defn handle-post-jots [request]
  (let [jot (m/fetch-one :budjot :where {:name (:name (:body request))})]
    (if (not= nil jot)
      {:status 409}
      {:status 201 :body (json/write-str (insert-jot (:body request)))})))

(defn handle-post-users [request]
  {:status 201
   :body (json/write-str (:body request))})

(defn handle-post [request]
  (case (:uri request)
    "/jots" (handle-post-jots request)
    "/users" (handle-post-users request)
    {:status 400}))

(defn handle-get [request]
  {:status 200 :body (json/write-str {:message "you got me"})})

(defn handle-put [request]
  {:status 200 :body (json/write-str {:message "you'll have to put up with this"})})

(defn handle-delete[request]
  {:status 204})

(defn handler [request]
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
