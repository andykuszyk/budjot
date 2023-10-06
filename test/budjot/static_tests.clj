(ns budjot.static-tests
  (:require [clojure.test :as test]
            [clj-http.client :as client]
            [budjot.fixtures :as fixtures]))

(test/use-fixtures :once fixtures/integration-test-fixture)

(test/deftest get-index
  (let [response (client/get "http://localhost:8080/index.html")]
    (test/is (= (:status response) 200))
    (test/is (.startsWith (:body response) "<!doctype html>"))))

(test/deftest get-unfound-file
  (test/is (thrown-with-msg? Exception #"status 404" (client/get "http://localhost:8080/notfound.foo"))))
  
