(ns budjot.post-jots-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [clj-http.client :as client]
            [clojure.java.shell :as shell]
            [budjot.main :refer [start-budjot]]))

(defn start-mongo []
  (let [result (shell/sh "docker" "run" "--name" "budjot-mongo" "-d" "-p" "27017:27017" "mongo:6")]
    (assert (= 0 (:exit result)) (format "error starting mongo (stderr): %1s" (:err result)))
    "mongodb://localhost:27017/budjot"))

(defn stop-mongo []
  (let [result (shell/sh "docker" "rm" "-f" "budjot-mongo")]
    (assert (= 0 (:exit result)) (format "error stopping mongo (stderr): %1s" (:err result)))))

(defn fixture [f]
  (stop-mongo)
  (let [mongo-address (start-mongo)
        server (start-budjot false 8080 mongo-address)] 
    (f)
    (.stop server)
    (stop-mongo)))

(use-fixtures :once fixture)

(def url "http://localhost:8080/jots")

(defn jot [name]
  {:name name
   :income 1000
   :userid "abc123"
   :entries [{:name "shopping" :amount "25":paid false}
             {:name "fuel" :amount "70" :paid true}]})

(defn post-request [j]
  {:form-params j :content-type :json})

(deftest post-jots-returns-201-and-body
  (let [response (client/post url (post-request (jot "new jot")))]
    (is (= 201 (:status response)))
    (let [actual (json/read-str (:body response) :key-fn keyword)
          expected (jot "new jot")]
      (is (= (:name actual) (:name expected)))
      (is (= (:income actual) (:income expected)))
      (is (= (:userid actual) (:userid expected)))
      (is (= (:entries actual) (:entries expected)))
      (is (>= (.length (:_id actual)) 3)))))

(deftest post-bad-url-returns-400
  (is (thrown-with-msg? Exception #"status 400" (client/post "http://localhost:8080"))))

(deftest post-jots-with-same-name-returns-409
  (is (= 201 (:status (client/post url  (post-request (jot "409"))))))
  (is (thrown-with-msg? Exception #"status 409" (client/post url  (post-request (jot "409"))))))
