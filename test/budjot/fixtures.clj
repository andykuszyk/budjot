(ns budjot.fixtures
  (:require [clojure.java.shell :as shell]
            [clojure.data.json :as json]
            [clj-http.client :as client]
            [budjot.main :as budjot]))

(def local-mongo-url "mongodb://localhost:27017/budjot")

(def budjot-url "http://localhost:8080/jots")

(defn start-mongo []
  (let [result (shell/sh "docker" "run" "--name" "budjot-mongo" "-d" "-p" "27017:27017" "mongo:6")]
    (assert (= 0 (:exit result)) (format "error starting mongo (stderr): %1s" (:err result)))
    local-mongo-url))

(defn stop-mongo []
  (let [result (shell/sh "docker" "rm" "-f" "budjot-mongo")]
    (assert (= 0 (:exit result)) (format "error stopping mongo (stderr): %1s" (:err result)))))

(defn integration-test-fixture [f]
  (stop-mongo)
  (let [mongo-address (start-mongo)
        server (budjot/start-budjot false 8080 mongo-address)] 
    (f)
    (.stop server)
    (budjot/stop-budjot)
    (stop-mongo)))

(defn build-jot [name]
  {:name name
   :income 1000
   :userid "abc123"
   :entries [{:name "shopping" :amount "25":paid false}
             {:name "fuel" :amount "70" :paid true}]})

(defn generate-user-id []
  (.toString (java.util.UUID/randomUUID)))

(defn build-post-jot-request
  ([jot]
   (build-post-jot-request jot (generate-user-id)))
  ([jot user-id]
   {:form-params jot :content-type :json :headers {"Authorization" user-id}}))

(defn generate-name []
  (.toString (java.util.UUID/randomUUID)))

(defn post-jot []
  (json/read-str
   (:body
    (client/post
     budjot-url
     (build-post-jot-request (build-jot (generate-name)))))
   :key-fn keyword))
