(ns budjot.fixtures
  (:require [clojure.java.shell :as shell]
            [budjot.main :refer [start-budjot]]))

(defn budjot-url []
  "http://localhost:8080/jots")

(defn start-mongo []
  (let [result (shell/sh "docker" "run" "--name" "budjot-mongo" "-d" "-p" "27017:27017" "mongo:6")]
    (assert (= 0 (:exit result)) (format "error starting mongo (stderr): %1s" (:err result)))
    "mongodb://localhost:27017/budjot"))

(defn stop-mongo []
  (let [result (shell/sh "docker" "rm" "-f" "budjot-mongo")]
    (assert (= 0 (:exit result)) (format "error stopping mongo (stderr): %1s" (:err result)))))

(defn integration-test-fixture [f]
  (stop-mongo)
  (let [mongo-address (start-mongo)
        server (start-budjot false 8080 mongo-address)] 
    (f)
    (.stop server)
    (stop-mongo)))
