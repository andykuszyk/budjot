(ns budjot.put-jots-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [budjot.fixtures :as f]
            [clj-http.client :as client]
            [clojure.data.json :as json]))

(use-fixtures :once f/integration-test-fixture)

(deftest put-jot-that-exists
  (let [posted-jot (f/post-jot)
        updated-jot (merge posted-jot {:name "this is an update"})
        jot-url (format "http://localhost:8080/jots/%1s" (:id posted-jot))
        response (client/put jot-url {:form-params updated-jot :content-type :json})
        got-jot (json/read-str (:body (client/get jot-url)) :key-fn keyword)]
    (is (= (:status response) 204))
    (is (= (:name got-jot) (:name updated-jot)))))

(deftest put-jot-that-does-not-exist
  (is (thrown-with-msg? Exception #"status 404" (client/put
                                                 "http://localhost:8080/jots/6482284c0dc0525a1bb98786"
                                                 {:form-params (f/build-jot "foo") :content-type :json}))))

(deftest put-jot-invalid-url
    (is (thrown-with-msg? Exception #"status 400" (client/put "http://localhost:8080/jots/"))))

(deftest put-jot-invalid-body
  (let [posted-jot (f/post-jot)
        updated-jot (dissoc posted-jot :name)
        jot-url (format "http://localhost:8080/jots/%1s" (:id posted-jot))]
    (is (thrown-with-msg? Exception #"status 400" (client/put jot-url {:form-params updated-jot :content-type :json})))))
