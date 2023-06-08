(ns budjot.get-jots-test
  (:require [clojure.test :refer :all]
            [budjot.fixtures :refer :all]
            [clojure.data.json :as json]
            [clj-http.client :as client]))

(use-fixtures :once integration-test-fixture)

(deftest get-jot-returns-200-and-jot
  (let [posted-jot (json/read-str (:body (client/post (budjot-url) (post-request (jot "get-test")))) :key-fn keyword)]
    (let [got-jot (json/read-str (:body (client/get (format "%1s/%2s" (budjot-url) (:_id posted-jot)))) :key-fn keyword)]
      (is (= posted-jot got-jot)))))

(deftest get-jot-doesnt-exist-returns-404
  (is (thrown-with-msg? Exception #"status 404" (client/get (format "%1s/6482284c0dc0525a1bb98786" (budjot-url))))))

(deftest get-jot-malformed-id-returns-400
  (is (thrown-with-msg? Exception #"status 400" (client/get "http://localhost:8080/jotsabc123abc123abc123abc123"))))

(deftest get-jot-malformed-uri-returns-400
  (is (thrown-with-msg? Exception #"status 400" (client/get "http://localhost:8080/jot/abc123abc123abc123abc123"))))
