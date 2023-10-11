(ns budjot.get-jots-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [budjot.fixtures :as f]
            [budjot.jots :as jots]
            [clojure.data.json :as json]
            [clj-http.client :as client]))

(use-fixtures :once f/integration-test-fixture)

(deftest get-jot-by-id-returns-200-and-jot
  (let [posted-jot (f/post-jot)
        got-jot (json/read-str
                 (:body
                  (client/get (format "%1s/%2s" f/budjot-url (:id posted-jot))))
                 :key-fn keyword)]
    (is (= posted-jot got-jot))))

(deftest get-jots-returns-200-and-jots
  (let [
        posted-jots (doall (for [i (range 0 10)] (f/post-jot)))
        got-jots (json/read-str (:body (client/get f/budjot-url)) :key-fn keyword)
        ]
    (is (= posted-jots got-jots))))

(deftest get-jot-doesnt-exist-returns-404
  (is (thrown-with-msg? Exception #"status 404" (client/get (format "%1s/6482284c0dc0525a1bb98786" f/budjot-url)))))

(deftest get-jot-malformed-id-returns-400
  (is (thrown-with-msg? Exception #"status 400" (client/get "http://localhost:8080/jotsabc123abc123abc123abc123"))))

(deftest get-jot-malformed-uri-returns-404
  (is (thrown-with-msg? Exception #"status 404" (client/get "http://localhost:8080/jot/abc123abc123abc123abc123"))))

(deftest get-jot-id-well-formed-uri
  (is (= "abc123" (jots/get-jot-id "/jots/abc123"))))

(deftest get-jot-id-malformed-uri
  (is (= nil (jots/get-jot-id "/jotsabc123")))
  (is (= nil (jots/get-jot-id "/jot/abc123"))))
