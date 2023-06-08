(ns budjot.post-jots-test
  (:require [clojure.test :refer :all]
            [budjot.fixtures :refer [integration-test-fixture, budjot-url]]
            [clojure.data.json :as json]
            [clj-http.client :as client]))

(use-fixtures :once integration-test-fixture)

(defn jot [name]
  {:name name
   :income 1000
   :userid "abc123"
   :entries [{:name "shopping" :amount "25":paid false}
             {:name "fuel" :amount "70" :paid true}]})

(defn post-request [j]
  {:form-params j :content-type :json})

(deftest post-jots-returns-201-and-body
  (let [response (client/post budjot-url (post-request (jot "new jot")))]
    (is (= 201 (:status response)))
    (let [actual (json/read-str (:body response) :key-fn keyword)
          expected (jot "new jot")]
      (is (= (:name expected) (:name actual)))
      (is (= (:income expected) (:income actual)))
      (is (= (:userid expected) (:userid actual)))
      (is (= (:entries expected) (:entries actual)))
      (is (>= (.length (:createdOn actual)) 0))
      (is (>= (.length (:modifiedOn actual)) 0))
      (is (= 24 (.length (:_id actual)))))))

(deftest post-bad-url-returns-400
  (is (thrown-with-msg? Exception #"status 400" (client/post "http://localhost:8080"))))

(deftest post-jots-with-same-name-returns-409
  (is (= 201 (:status (client/post budjot-url  (post-request (jot "409"))))))
  (is (thrown-with-msg? Exception #"status 409" (client/post budjot-url  (post-request (jot "409"))))))
