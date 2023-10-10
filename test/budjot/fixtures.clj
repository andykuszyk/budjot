(ns budjot.fixtures
  (:require [clojure.java.shell :as shell]
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

(defn build-post-request [j]
  {:form-params j :content-type :json})
