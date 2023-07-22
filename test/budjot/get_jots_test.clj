(ns budjot.get-jots-test
  (:require [clojure.test :refer :all]
            [budjot.fixtures :refer :all]
            [clojure.data.json :as json]
            [clj-http.client :as client]))

(use-fixtures :once integration-test-fixture)

(defn generate-name []
  (.toString (java.util.UUID/randomUUID)))

(defn post-jot []
  (json/read-str (:body (client/post (budjot-url) (post-request (jot (generate-name))))) :key-fn keyword))

(deftest get-jot-by-id-returns-200-and-jot
  (let [posted-jot (post-jot)]
    (let [got-jot (json/read-str (:body (client/get (format "%1s/%2s" (budjot-url) (:_id posted-jot)))) :key-fn keyword)]
      (is (= posted-jot got-jot)))))

(deftest get-jots-returns-200-and-jots
  (let [posted-jots (for [i (range 0 10)] (post-jot))]
    (let [got-jots (json/read-str (:body (client/get (budjot-url))) :key-fn keyword)]
      (is (= (count posted-jots) (count got-jots))))))

(deftest get-jot-doesnt-exist-returns-404
  (is (thrown-with-msg? Exception #"status 404" (client/get (format "%1s/6482284c0dc0525a1bb98786" (budjot-url))))))

(deftest get-jot-malformed-id-returns-400
  (is (thrown-with-msg? Exception #"status 400" (client/get "http://localhost:8080/jotsabc123abc123abc123abc123"))))

(deftest get-jot-malformed-uri-returns-400
  (is (thrown-with-msg? Exception #"status 400" (client/get "http://localhost:8080/jot/abc123abc123abc123abc123"))))
