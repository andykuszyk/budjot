(ns budjot.main
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as ring-json]
            [somnium.congomongo :as mongo]
            [clojure.string :as string]
            [taoensso.timbre :as log]
            [ring.middleware.resource :as resource]
            [budjot.jots :as jots]
            [budjot.users :as users])
  (:gen-class))

(defn handler [request]
  (case (:request-method request)
    :get (do
           (if (string/starts-with? (:uri request) "/jots")
             (jots/handle-get )
             {:status 404}))
    :post (case (:uri request)
            "/jots" (jots/handle-post request)
            "/users" (users/handle-post request)
            {:status 400})
    :put {:status 405 :body "not implemented yet"}
    :delete {:status 405 :body "not implemented yet"}))

(defn start-budjot [join? port mongo-address]
  (def mongo-connection (mongo/make-connection mongo-address))
  (mongo/set-connection! mongo-connection)
  (jetty/run-jetty (resource/wrap-resource (ring-json/wrap-json-body handler {:keywords? true}) "") {:join? join? :port port}))

(defn stop-budjot []
  (mongo/close-connection mongo-connection))

(defn- get-mongo-address []
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
