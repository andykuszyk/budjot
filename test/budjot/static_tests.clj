(ns budjot.static-tests
  (:require [clojure.test :as test]
            [clj-http.client :as client]
            [budjot.fixtures :as fixtures]))

(test/use-fixtures :once fixtures/integration-test-fixture)

(test/deftest get-root
  (let [response (client/get "http://localhost:8080/")]
    (test/is (= (:status response) 200))
    (test/is (= (:body response) "html"))))
