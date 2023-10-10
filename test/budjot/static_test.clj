(ns budjot.static-test
  (:require [clojure.test :refer [deftest use-fixtures is]]
            [clj-http.client :as client]
            [budjot.fixtures :as fixtures]))

(use-fixtures :once fixtures/integration-test-fixture)

(deftest get-index
  (let [response (client/get "http://localhost:8080/index.html")]
    (is (= (:status response) 200))
    (is (.startsWith (:body response) "<!doctype html>"))))

(deftest get-unfound-file
  (is (thrown-with-msg? Exception #"status 404" (client/get "http://localhost:8080/notfound.foo"))))
  
