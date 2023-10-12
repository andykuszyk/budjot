(ns budjot.delete-jots-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [budjot.fixtures :as f]
            [clj-http.client :as client]))

(use-fixtures :once f/integration-test-fixture)

(deftest delete-jots-by-id-exists
  (let [posted-jot (f/post-jot)
        jot-url (format "http://localhost:8080/jots/%1s" (:id posted-jot))
        response (client/delete jot-url)]
    (is (= (:status response) 202))
    (is (thrown-with-msg? Exception #"status 404" (client/get jot-url)))))

(deftest delete-jots-by-id-not-exists
    (is (thrown-with-msg? Exception #"status 404" (client/delete "http://localhost:8080/jots/6482284c0dc0525a1bb98786"))))

(deftest delete-jots-by-id-not-valid
    (is (thrown-with-msg? Exception #"status 400" (client/delete "http://localhost:8080/jots/"))))
